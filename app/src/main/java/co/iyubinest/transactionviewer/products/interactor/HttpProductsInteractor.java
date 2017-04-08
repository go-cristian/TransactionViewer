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
package co.iyubinest.transactionviewer.products.interactor;

import co.iyubinest.transactionviewer.products.GBPConversion;
import co.iyubinest.transactionviewer.products.Product;
import co.iyubinest.transactionviewer.transactions.Transaction;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class HttpProductsInteractor implements ProductsInteractor {

  private final ProductsService service;

  public HttpProductsInteractor(Retrofit retrofit) {
    service = retrofit.create(ProductsService.class);
  }

  @Override
  public Flowable<List<Product>> all() {
    return Flowable.zip(service.rates(), service.transactions(), this::zip);
  }

  private List<Product> zip(List<RateResponse> rateResponses,
    List<TransactionResponse> transactionResponses) {
    HashMap<String, List<Transaction>> transactions = new HashMap<>();
    GBPConversion conversion = new GBPConversion(rateResponses);
    for (TransactionResponse transaction : transactionResponses) {
      if (!transactions.containsKey(transaction.sku)) {
        transactions.put(transaction.sku, new LinkedList<>());
      }
      String original = transaction.currency + transaction.amount;
      Double value = conversion.convert(Double.valueOf(transaction.amount), transaction.currency);
      List<Transaction> transactionList = transactions.get(transaction.sku);
      transactionList.add(Transaction.create(original, transaction.currency, value));
    }
    return toList(transactions);
  }

  private List<Product> toList(HashMap<String, List<Transaction>> transactions) {
    ArrayList<Product> products = new ArrayList<>(transactions.size());
    for (String sku : transactions.keySet()) {
      products.add(Product.create(sku,
        transactions.get(sku),
        "GBP" + getGrandTotal(transactions.get(sku))
      ));
    }
    return products;
  }

  private double getGrandTotal(List<Transaction> transactions) {
    double grandTotal = 0;
    for (Transaction transaction : transactions) {
      grandTotal += transaction.value();
    }
    return grandTotal;
  }

  interface ProductsService {

    @GET("rates")
    Flowable<List<RateResponse>> rates();
    @GET("transactions")
    Flowable<List<TransactionResponse>> transactions();
  }

  public static class RateResponse {

    public String from, rate, to;
  }

  private static class TransactionResponse {

    String amount, sku, currency;
  }
}
