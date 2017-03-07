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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.iyubinest.transactionviewer.BaseActivity;
import co.iyubinest.transactionviewer.R;
import co.iyubinest.transactionviewer.transactions.TransactionsActivity;
import java.util.List;
import javax.inject.Inject;

public class ProductsActivity extends BaseActivity implements ProductsView {

  @Inject
  ProductsPresenter presenter;
  @BindView(R.id.products_list)
  ProductsWidget productsView;
  @BindView(R.id.products_retry)
  View retryView;
  @BindView(R.id.loading)
  View loadingView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.products_activity);
    ButterKnife.bind(this);
    appComponent().productsComponent(new ProductsModule(this)).inject(this);
    presenter.init();
  }

  @Override
  public void showLoading() {
    show(loadingView);
  }

  @Override
  public void showRetry() {
    show(retryView);
  }

  @Override
  public void showProducts(List<Product> products) {
    show(productsView);
    productsView.add(products);
    productsView.setListener(this::showTransactions);
  }

  @OnClick(R.id.products_retry_button)
  public void retry() {
    presenter.init();
  }

  private void showTransactions(Product product) {
    startActivity(TransactionsActivity.create(this, product));
  }

  private void show(View view) {
    loadingView.setVisibility(View.INVISIBLE);
    productsView.setVisibility(View.INVISIBLE);
    retryView.setVisibility(View.INVISIBLE);
    view.setVisibility(View.VISIBLE);
  }
}
