package com.theostriches.amaretto.android.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.theostriches.amaretto.android.R;
import com.theostriches.amaretto.android.model.Event;


public class ProductsFragment extends ListFragment implements OnItemClickListener {

	private ProductsActivity mMain;

	private QuantifiedCategory category;
	private ItemListAdapter adapter;
	private TextView emptyT;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mMain = (ProductsActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		category = mMain.getCategory();
		View v = inflater.inflate(R.layout.fragment_products, container, false);
		adapter = new ItemListAdapter(category.getProducts());
		emptyT = (TextView) v.findViewById(android.R.id.empty);
		setListAdapter(adapter);
		return v;
	}

	@Override
	public void onStart() {
		getListView().setOnItemClickListener(this);
		getListView().setEmptyView(emptyT);
		super.onStart();
	}

	public class ItemListAdapter extends ArrayAdapter<Event> {

		private List<Event> mListProducts;
		 

		public ItemListAdapter(List<Event> listLibros) {
			super(mMain, 0);
			this.mListProducts = listLibros;
		}

		public int getCount() {
			return mListProducts.size();
		}

		public Event getItem(int position) {
			return mListProducts.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mMain.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.fragment_products_row, null);
			} else {
				v = convertView;
			}
			final Event p = getItem(position);
			if (p != null) {
			
			}
			return v;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		QuantifiedProduct p = adapter.getItem(position);
		p.setQuantity(p.getQuantity() + 1);
		adapter.notifyDataSetChanged();
	}

}
