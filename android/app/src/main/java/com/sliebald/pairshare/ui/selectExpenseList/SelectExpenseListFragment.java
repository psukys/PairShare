package com.sliebald.pairshare.ui.selectExpenseList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.sliebald.pairshare.R;
import com.sliebald.pairshare.data.Repository;
import com.sliebald.pairshare.databinding.FragmentSelectExpenseListBinding;

public class SelectExpenseListFragment extends Fragment {

    private SelectExpenseListViewModel mViewModel;

    private FirestoreRecyclerAdapter expenseListsAdapter;

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

        expenseListsAdapter = Repository.getInstance().getExpenseListsAdapter();

        mBinding.rvActiveLists.setAdapter(expenseListsAdapter);
        mBinding.rvActiveLists.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onStart() {
        super.onStart();
        expenseListsAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        expenseListsAdapter.stopListening();
    }


}
