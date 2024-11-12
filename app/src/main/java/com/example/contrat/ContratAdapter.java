package com.example.contrat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.contrat.entity.Contrat;

import java.util.List;

public class ContratAdapter extends RecyclerView.Adapter<ContratAdapter.ContratViewHolder> {

    private List<Contrat> contratList;
    private OnItemClickListener listener;

    // Constructor with click listener
    public ContratAdapter(List<Contrat> contratList, OnItemClickListener listener) {
        this.contratList = contratList;
        this.listener = listener;
    }

    // ViewHolder class
    public static class ContratViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivContratImage;
        public TextView tvTitle, tvDescription;
        public Button btnViewDescription;

        public ContratViewHolder(View itemView) {
            super(itemView);
            ivContratImage = itemView.findViewById(R.id.ivContratImage); // Correct ImageView ID
            tvTitle = itemView.findViewById(R.id.tvTitle);           // Correct TextView ID for Title
            tvDescription = itemView.findViewById(R.id.tvDescription); // Correct TextView ID for Description
            btnViewDescription = itemView.findViewById(R.id.btnViewDescription); // Correct Button ID
        }
    }

    @Override
    public ContratViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout for each contract
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrat, parent, false);
        return new ContratViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContratViewHolder holder, int position) {
        Contrat currentContrat = contratList.get(position);
        holder.tvTitle.setText(currentContrat.getContractType());  // Assuming 'getTitle()' should be 'getType()'
        holder.tvDescription.setText(currentContrat.getDescription());

        // If you don't have an image resource, you can comment out the line below or set a placeholder
        // holder.ivContratImage.setImageResource(currentContrat.getImageResId());

        // Handle click event for the description button
        holder.btnViewDescription.setOnClickListener(v -> {
            listener.onItemClick(currentContrat); // Pass the current contract to the listener
        });
    }

    @Override
    public int getItemCount() {
        return contratList.size();
    }

    // Define the listener interface for item clicks
    public interface OnItemClickListener {
        void onItemClick(Contrat contrat);
    }
}
