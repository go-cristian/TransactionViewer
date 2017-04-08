/**
 * Copyright (C) 2017 Cristian Gomez Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.iyubinest.transactionviewer.products;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import co.iyubinest.transactionviewer.App;
import co.iyubinest.transactionviewer.DaggerRule;
import co.iyubinest.transactionviewer.R;
import co.iyubinest.transactionviewer.products.interactor.ProductsInteractor;
import co.iyubinest.transactionviewer.products.view.ProductsActivity;
import co.iyubinest.transactionviewer.transactions.Transaction;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static co.iyubinest.transactionviewer.assertions.RecyclerViewActions.clickAt;
import static co.iyubinest.transactionviewer.assertions.RecyclerViewActions.scrollTo;
import static co.iyubinest.transactionviewer.assertions.RecyclerViewAssertions.count;
import static co.iyubinest.transactionviewer.assertions.RecyclerViewAssertions.item;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ProductsActivityShould {

  private static final int TOTAL = 10;
  private static List<Product> products = new ArrayList<>(TOTAL);
  private static Flowable<List<Product>> productsResult =
    Flowable.defer(() -> Flowable.just(products));
  private static Flowable error = Flowable.error(new Exception());

  static {
    for (int i = 0; i < TOTAL; i++) {
      List<Transaction> transactions = new ArrayList<>(TOTAL);
      double grandTotal = 0;
      for (int j = 0; j < TOTAL; j++) {
        double value = j + 100;
        Transaction transaction = Transaction.create("$" + (j + 1), "$", value);
        transactions.add(transaction);
        grandTotal = grandTotal + value;
      }
      Product product = Product.create("Product " + i, transactions, "$" + grandTotal);
      products.add(product);
    }
  }

  @Rule
  public ActivityTestRule rule = new ActivityTestRule<>(ProductsActivity.class, false, false);
  @Rule
  public DaggerRule daggerRule = new DaggerRule();
  @Mock
  public ProductsInteractor interactor;

  @Test
  public void showErrorWhenFailure() throws Exception {
    when(interactor.all()).thenReturn(error);
    rule.launchActivity(new Intent());
    onViewId(R.id.products_list).check(matches(not(isDisplayed())));
    onViewText(R.string.products_retry_msg).check(matches(isDisplayed()));
  }

  private ViewInteraction onViewId(@IdRes int idRes) {return onView(withId(idRes));}

  private ViewInteraction onViewText(@StringRes int stringRes) {return onView(withText(stringRes));}

  @Test
  public void showProductsWhenSuccess() throws Exception {
    when(interactor.all()).thenReturn(productsResult);
    rule.launchActivity(new Intent());
    onViewId(R.id.products_list).check(matches(isDisplayed()));
    onViewId(R.id.products_retry_msg).check(matches(not(isDisplayed())));
    onViewId(R.id.products_list).check(count(TOTAL));
    //check all elements on recycler view
    for (int productCount = 0; productCount < TOTAL; productCount++) {
      int size = products.get(productCount).transactions().size();
      String sizeString = app().getString(R.string.products_item_count_format, size);
      Product product = products.get(productCount);
      onViewId(R.id.products_list).perform(scrollTo(productCount));
      onViewId(R.id.products_list).check(item(productCount, R.id.products_item_sku, product.sku()));
      onViewId(R.id.products_list).check(item(productCount, R.id.products_item_count, sizeString));
      onViewId(R.id.products_list).perform(clickAt(productCount));
      //check recyclerview on new screen
      onViewId(R.id.transactions_list).check(matches(isDisplayed()));
      onViewId(R.id.transactions_list).check(count(TOTAL));
      onViewId(R.id.transactions_total).check(matches(withText(product.grandTotal())));
      for (int transactionCount = 0; transactionCount < TOTAL; transactionCount++) {
        Transaction transaction = product.transactions().get(transactionCount);
        onViewId(R.id.transactions_list).perform(scrollTo(transactionCount));
        onViewId(R.id.transactions_list).check(item(transactionCount,
          R.id.transactions_item_original,
          transaction.original()
        ));
        onViewId(R.id.transactions_list).check(item(transactionCount,
          R.id.transactions_item_value,
          "GBP" + transaction.value()
        ));
      }
      Espresso.pressBack();
    }
  }

  private static App app() {
    return (App) InstrumentationRegistry.getInstrumentation()
      .getTargetContext()
      .getApplicationContext();
  }
}
