package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.useful.nfcclient.R;
import ca.useful.nfcclient.databinding.ItemTextviewBinding;
import connector.ConnectorExampleC;

public class RecyclerAdapterExampleA extends RecyclerView.Adapter<RecyclerAdapterExampleA.ViewHolder> {

    private ConnectorExampleC connectorExampleC;
    private List<String> stringList;

    public RecyclerAdapterExampleA(ConnectorExampleC connectorExampleC, List<String> stringList) {
        this.connectorExampleC = connectorExampleC;
        this.stringList = stringList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = stringList.get(holder.getBindingAdapterPosition());
        holder.binding.itTextview.setText(item);
        holder.binding.getRoot().setOnClickListener(v -> {
            connectorExampleC.doClickOnItem(stringList.get(holder.getBindingAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTextviewBinding binding;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTextviewBinding.bind(itemView);
        }
    }
}
