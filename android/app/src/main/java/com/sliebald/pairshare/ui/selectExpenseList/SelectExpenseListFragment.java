package com.sliebald.pairshare.ui.selectExpenseList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.sliebald.pairshare.R;
import com.sliebald.pairshare.data.Repository;
import com.sliebald.pairshare.data.models.ExpenseList;
import com.sliebald.pairshare.databinding.FragmentSelectExpenseListBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SelectExpenseListFragment extends Fragment {

    private SelectExpenseListViewModel mViewModel;

    /**
     * Databinding of the corresponding fragment layout.
     */
    private FragmentSelectExpenseListBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_select_expense_list, container, false);
        mBinding.fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_selectExpense_dest_to_addExpenseListFragment));
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SelectExpenseListViewModel.class);


        Query query = Repository.getInstance().getActiveExpenseListsQuery();

        FirestoreRecyclerOptions<ExpenseList> options =
                new FirestoreRecyclerOptions.Builder<ExpenseList>()
                        .setQuery(query, ExpenseList.class)
                        .setLifecycleOwner(this)
                        .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<ExpenseList,
                ExpenseListHolder>(options) {
            @Override
            public void onBindViewHolder(ExpenseListHolder holder, int position,
                                         @NonNull ExpenseList expenseList) {
                holder.bind(expenseList);
            }

            @Override
            public ExpenseListHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.recycler_item_expense_list, group, false);
                return new ExpenseListHolder(view);
            }
        };

        mBinding.rvActiveExpense.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mBinding.rvActiveExpense.setLayoutManager(manager);
    }


}
