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

public class ProductsWidget extends RecyclerView {

  private ProductsAdapter adapter;

  public ProductsWidget(Context context) {
    this(context, null);
  }

  public ProductsWidget(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    setLayoutManager(new LinearLayoutManager(getContext()));
    setHasFixedSize(true);
  }

  public void add(List<Product> products) {
    adapter = new ProductsAdapter(products);
    setAdapter(adapter);
  }

  public void setListener(ProductsAdapter.OnProductSelected listener) {
    adapter.setListener(listener);
  }

  static class ProductsAdapter extends RecyclerView.Adapter<ProductsHolder> {

    interface OnProductSelected {

      void onSelected(Product product);
    }

    private List<Product> products = new ArrayList<>();
    private OnProductSelected listener;

    public ProductsAdapter(List<Product> products) {
      this.products.addAll(products);
    }

    @Override
    public ProductsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ProductsHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.products_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductsHolder holder, int position) {
      Product product = products.get(position);
      holder.product(product);
      holder.setListener(selected -> {
        if (listener != null) listener.onSelected(products.get(selected));
      });
    }

    @Override
    public int getItemCount() {
      return products.size();
    }

    public void setListener(OnProductSelected listener) {
      this.listener = listener;
    }
  }

  static class ProductsHolder extends RecyclerView.ViewHolder {

    interface OnPositionSelected {

      void onSelecion(int position);
    }

    @BindView(R.id.products_item_sku)
    TextView skuView;
    @BindView(R.id.products_item_count)
    TextView countView;
    private OnPositionSelected listener;

    public ProductsHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(v -> {
        if (listener != null) listener.onSelecion(getAdapterPosition());
      });
    }

    public void product(Product product) {
      String count = itemView.getContext()
        .getString(R.string.products_item_count_format, product.transactions().size());
      skuView.setText(product.sku());
      countView.setText(count);
    }

    public void setListener(OnPositionSelected listener) {
      this.listener = listener;
    }
  }
}
