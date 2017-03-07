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
package co.iyubinest.transactionviewer.assertions;

import android.support.annotation.IdRes;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RecyclerViewAssertions {

  public static ViewAssertion count(int count) {
    return (view, noViewFoundException) -> {
      if (noViewFoundException != null) {
        throw noViewFoundException;
      }
      RecyclerView recyclerView = (RecyclerView) view;
      RecyclerView.Adapter adapter = recyclerView.getAdapter();
      assertThat(adapter.getItemCount(), is(count));
    };
  }

  public static ViewAssertion item(int position, @IdRes int layoutId, String string) {
    return (view, noViewFoundException) -> {
      if (noViewFoundException != null) {
        throw noViewFoundException;
      }
      RecyclerView recyclerView = (RecyclerView) view;
      RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
      layoutManager.findViewByPosition(position);
      TextView textView =
        (TextView) (layoutManager.findViewByPosition(position)).findViewById(layoutId);
      assertThat(textView.getText().toString(), is(string));
    };
  }
}