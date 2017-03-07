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
package co.iyubinest.transactionviewer.transactions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.iyubinest.transactionviewer.R;
import java.util.ArrayList;
import java.util.List;

public class TransactionsWidget extends RecyclerView {

  private TransactionsAdapter adapter;

  public TransactionsWidget(Context context) {
    this(context, null);
  }

  public TransactionsWidget(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    setLayoutManager(new LinearLayoutManager(getContext()));
    setHasFixedSize(true);
  }

  public void add(List<Transaction> transactions) {
    adapter = new TransactionsWidget.TransactionsAdapter(transactions);
    setAdapter(adapter);
  }

  static class TransactionsAdapter
    extends RecyclerView.Adapter<TransactionsWidget.TransactionsHolder> {

    private List<Transaction> transactions = new ArrayList<>();

    public TransactionsAdapter(List<Transaction> transactions) {
      this.transactions.addAll(transactions);
    }

    @Override
    public TransactionsWidget.TransactionsHolder onCreateViewHolder(ViewGroup parent,
      int viewType) {
      return new TransactionsWidget.TransactionsHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.transactions_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TransactionsWidget.TransactionsHolder holder, int position) {
      Transaction product = transactions.get(position);
      holder.transaction(product);
    }

    @Override
    public int getItemCount() {
      return transactions.size();
    }
  }

  static class TransactionsHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.transactions_item_original)
    TextView originalView;
    @BindView(R.id.transactions_item_value)
    TextView valueView;

    public TransactionsHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void transaction(Transaction transaction) {
      originalView.setText(transaction.original());
      valueView.setText("GBP" + transaction.value());
    }
  }
}
