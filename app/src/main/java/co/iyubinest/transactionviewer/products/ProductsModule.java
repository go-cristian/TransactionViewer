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

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ProductsModule {

  private final ProductsActivity activity;

  public ProductsModule(ProductsActivity activity) {this.activity = activity;}

  @Provides
  public ProductsView productsActivity() {
    return activity;
  }

  @Provides
  public ProductsInteractor productsInteractor(Retrofit retrofit) {
    return new AndroidHttpProductsInteractor(new HttpProductsInteractor(retrofit));
  }
}
