package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.useful.nfcclient.R;
import ca.useful.nfcclient.databinding.ItemDocumentFileBinding;
import connector.ConnectorExampleI;
import dataroom.dto.DocumentFileOverarchPortDTO;

public class DocumentFileRecyclerAdapter extends RecyclerView.Adapter<DocumentFileRecyclerAdapter.ViewHolder> {

    private ConnectorExampleI connectorExampleI;
    private List<DocumentFileOverarchPortDTO.DocumentDetail> documentDetails = null;
    private List<DocumentFile> documentFiles = null;
    private DocumentFileOverarchPortDTO dto;

    public DocumentFileRecyclerAdapter(ConnectorExampleI connectorExampleI, DocumentFileOverarchPortDTO dto) {
        this.connectorExampleI = connectorExampleI;
        this.documentDetails = dto.documentDetails;
        this.dto = dto;
    }

    public DocumentFileRecyclerAdapter(List<DocumentFile> documentFiles) {
        this.documentFiles = documentFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (documentFiles != null) {
            DocumentFile df = documentFiles.get(holder.getBindingAdapterPosition());
            holder.binding.idfText.setText(df.getName());
            holder.binding.idfType.setText(df.getType() != null ? df.getType() : "");
            holder.binding.getRoot().setOnClickListener(v -> {
//                DocumentFile file = documentFiles.get(holder.getBindingAdapterPosition());
//                if (dto != null) {
//                    dto.chosenDocumentFile = file;
//                    connectorExampleI.chooseDocumentFile(dto);
//                }
            });
        } else if (documentDetails != null) {
            //denotes we are operating with a virtual directory
            DocumentFileOverarchPortDTO.DocumentDetail detail = documentDetails.get(holder.getBindingAdapterPosition());
            holder.binding.idfText.setText(detail.name);
            holder.binding.idfType.setText(detail.type);
            holder.binding.getRoot().setOnClickListener(v -> {
                DocumentFileOverarchPortDTO.DocumentDetail chosenDetail = documentDetails.get(holder.getBindingAdapterPosition());
                if (dto != null) {
                    dto.chosenDocumentDetail = chosenDetail;
                    connectorExampleI.chooseDocumentFile(dto);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return documentFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDocumentFileBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemDocumentFileBinding.bind(itemView);
        }
    }
}
