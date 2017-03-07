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

import co.iyubinest.transactionviewer.transactions.Transaction;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ProductsPresenterTest {

  private static final int TOTAL = 100;
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

  @Mock
  private ProductsView view;
  @Mock
  private ProductsInteractor interactor;
  private ProductsPresenter presenter;

  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);
    presenter = new ProductsPresenter(view, interactor);
  }

  @Test
  public void showError() throws Exception {
    when(interactor.all()).thenReturn(error);
    presenter.init();
    verify(view, times(1)).showLoading();
    verify(view, times(1)).showRetry();
    verifyNoMoreInteractions(view);
  }

  @Test
  public void showProducts() throws Exception {
    when(interactor.all()).thenReturn(productsResult);
    presenter.init();
    verify(view, times(1)).showLoading();
    verify(view, times(1)).showProducts(eq(products));
    verifyNoMoreInteractions(view);
  }
}
