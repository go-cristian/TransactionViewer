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

import javax.inject.Inject;

public class ProductsPresenter {

  private final ProductsView view;
  private final ProductsInteractor interactor;

  @Inject
  public ProductsPresenter(ProductsView view, ProductsInteractor interactor) {
    this.view = view;
    this.interactor = interactor;
  }

  public void init() {
    view.showLoading();
    interactor.all().subscribe(view::showProducts, throwable -> view.showRetry());
  }
}
