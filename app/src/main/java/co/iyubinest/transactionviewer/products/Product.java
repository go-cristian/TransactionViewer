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

import android.os.Parcelable;
import co.iyubinest.transactionviewer.transactions.Transaction;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue
public abstract class Product implements Parcelable {

  public static Product create(String sku, List<Transaction> transactions, String grandTotal) {
    return new AutoValue_Product(sku, grandTotal, transactions);
  }

  public abstract String sku();
  public abstract String grandTotal();
  public abstract List<Transaction> transactions();
}
