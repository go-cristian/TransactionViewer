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

import co.iyubinest.transactionviewer.products.interactor.HttpProductsInteractor;
import co.iyubinest.transactionviewer.products.interactor.ProductsInteractor;
import co.iyubinest.transactionviewer.retrofit.AppRetrofit;
import co.iyubinest.transactionviewer.transactions.Transaction;
import io.reactivex.subscribers.TestSubscriber;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProductsInteractorTest {

  private static List<Product> firstProductsResult = new ArrayList<>(1);
  private static List<Transaction> firstTransactionsResult = new ArrayList<>(4);

  static {
    firstTransactionsResult.add(Transaction.create("GBP1", "GBP", 1.0));
    firstTransactionsResult.add(Transaction.create("EUR1", "EUR", 0.5));
    firstTransactionsResult.add(Transaction.create("USD1", "USD", 0.25));
    firstTransactionsResult.add(Transaction.create("AUD1", "AUD", 0.125));
    firstProductsResult.add(Product.create("J4064", firstTransactionsResult, "GBP1.875"));
  }

  private MockWebServer server;
  private Retrofit retrofit;
  private MockResponse ratesResponse = new MockResponse().setBody(fromFile("first/rates.json"));
  private MockResponse transactionsResponse =
    new MockResponse().setBody(fromFile("first/transactions.json"));
  private MockResponse error = new MockResponse().setHttp2ErrorCode(500);
  private TestSubscriber subscriber = new TestSubscriber();
  private ProductsInteractor interactor;

  private static String fromFile(String name) {
    try {
      InputStream resource =
        ProductsInteractorTest.class.getClassLoader().getResourceAsStream(name);
      BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
      StringBuilder result = new StringBuilder();
      String partial = reader.readLine();
      while (partial != null) {
        result.append(partial);
        partial = reader.readLine();
      }
      return result.toString();
    } catch (Exception ignored) {
      throw new IllegalArgumentException("File not found");
    }
  }

  @Before
  public void setup() throws Exception {
    server = new MockWebServer();
    retrofit = AppRetrofit.build(server.url("/").toString());
    interactor = new HttpProductsInteractor(retrofit);
  }

  @After
  public void tearDown() throws Exception {
    server.shutdown();
  }

  @Test
  public void failOnError() throws Exception {
    server.enqueue(error);
    interactor.all().subscribe(subscriber);
    subscriber.assertError(Exception.class);
  }

  @Test
  public void testSuccess() throws Exception {
    server.enqueue(ratesResponse);
    server.enqueue(transactionsResponse);
    interactor.all().subscribe(subscriber);
    List<Product> products = (List<Product>) subscriber.getEvents().get(0);
    assertThat(products.size(), is(1));
    assertThat(products.get(0), is(firstProductsResult));
  }
}
