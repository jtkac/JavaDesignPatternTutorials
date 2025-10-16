package cache;

import android.view.View;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import interfaces.OnDeletePressedIF;
import interfaces.OnSavePressedIF;

public class CacheExampleF extends BaseCache {
    private View.OnClickListener saveClickListener;
    private View.OnClickListener deleteClickListener;

    private View.OnClickListener saveButtonToggleListener;
    private View.OnClickListener deleteButtonToggleListener;

    private OnDeletePressedIF onDeletePressedIF;
    private OnSavePressedIF onSavePressedIF;

    private Integer saveVisibilityStatus;
    private Integer deleteVisibilityStatus;

    public CacheExampleF() {
        saveVisibilityStatus = View.VISIBLE;
        deleteVisibilityStatus = View.VISIBLE;

        saveClickListener = v -> {
            if (getOnSavePressedIF() != null) {
                getOnSavePressedIF().onSave();
            }
        };

        deleteClickListener = v -> {
            if (getOnDeletePressedIF() != null) {
                getOnDeletePressedIF().onDelete();
            }
        };

        setOnSavePressedIF(null);
        setOnDeletePressedIF(null);
    }

    @Bindable
    public View.OnClickListener getSaveClickListener() {
        return saveClickListener;
    }

    public void setSaveClickListener(View.OnClickListener saveClickListener) {
        this.saveClickListener = saveClickListener;
    }

    @Bindable
    public View.OnClickListener getDeleteClickListener() {
        return deleteClickListener;
    }

    public void setDeleteClickListener(View.OnClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public OnDeletePressedIF getOnDeletePressedIF() {
        return onDeletePressedIF;
    }

    public void setOnDeletePressedIF(OnDeletePressedIF onDeletePressedIF) {
        this.onDeletePressedIF = onDeletePressedIF;
        deleteVisibilityStatus = onDeletePressedIF != null ? View.VISIBLE : View.GONE;
        notifyPropertyChanged(BR.deleteVisibilityStatus);
    }

    public OnSavePressedIF getOnSavePressedIF() {
        return onSavePressedIF;
    }

    public void setOnSavePressedIF(OnSavePressedIF onSavePressedIF) {
        this.onSavePressedIF = onSavePressedIF;
        saveVisibilityStatus = onSavePressedIF != null ? View.VISIBLE : View.GONE;
        notifyPropertyChanged(BR.saveVisibilityStatus);
    }

    @Bindable
    public Integer getSaveVisibilityStatus() {
        return saveVisibilityStatus;
    }

    public void setSaveVisibilityStatus(Integer saveVisibilityStatus) {
        this.saveVisibilityStatus = saveVisibilityStatus;
    }

    @Bindable
    public Integer getDeleteVisibilityStatus() {
        return deleteVisibilityStatus;
    }

    public void setDeleteVisibilityStatus(Integer deleteVisibilityStatus) {
        this.deleteVisibilityStatus = deleteVisibilityStatus;
    }

    @Bindable
    public View.OnClickListener getSaveButtonToggleListener() {
        return saveButtonToggleListener;
    }

    public void setSaveButtonToggleListener(View.OnClickListener saveButtonToggleListener) {
        this.saveButtonToggleListener = saveButtonToggleListener;
    }

    @Bindable
    public View.OnClickListener getDeleteButtonToggleListener() {
        return deleteButtonToggleListener;
    }

    public void setDeleteButtonToggleListener(View.OnClickListener deleteButtonToggleListener) {
        this.deleteButtonToggleListener = deleteButtonToggleListener;
    }

}
