package com.gmail.hmazud.submissionkamus.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.hmazud.submissionkamus.Detail;
import com.gmail.hmazud.submissionkamus.R;
import com.gmail.hmazud.submissionkamus.db.KmsHelper;
import com.gmail.hmazud.submissionkamus.model.KmsModel;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<KmsModel> arrayList = new ArrayList<>();

    public SearchAdapter() {

    }

    public void replace(ArrayList<KmsModel> arrayItem) {
        arrayList = arrayItem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_list, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.textWord.setText(arrayList.get(position).getWord());
        holder.textTranslate.setText(arrayList.get(position).getTranslate());
        holder.bind(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textWord, textTranslate;

        public ViewHolder(View itemView) {
            super(itemView);
            textWord = itemView.findViewById(R.id.tv_word);
            textTranslate = itemView.findViewById(R.id.tv_translate);
        }

        public void bind(final KmsModel itemModel) {
            textWord.setText(itemModel.getWord());
            textTranslate.setText(itemModel.getTranslate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), Detail.class);
                    intent.putExtra(Detail.ITEM_WORD, itemModel.getWord());
                    intent.putExtra(Detail.ITEM_TRANSLATE, itemModel.getTranslate());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
