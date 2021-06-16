package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CategoryDialog extends AppCompatDialogFragment {
    private RadioGroup cat;
    private CatDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_category_dialog,null);
        cat = view.findViewById(R.id.categories);

        builder.setView(view)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String cattype = "";
                        switch (cat.getCheckedRadioButtonId()){
                            case R.id.radioButton:
                                cattype = "Houseware & Furnishing";
                                break;

                            case R.id.radioButton2:
                                cattype = "IT & Technology";
                                break;

                            case R.id.radioButton3:
                                cattype = "Baked Goods";
                                break;

                            case R.id.radioButton4:
                                cattype = "Hardware Store";
                                break;
                        }

                        listener.applyTexts(cattype);

                    }
                });




        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CatDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ExampleDialog Listener");
        }
    }

    public interface CatDialogListener{
        void applyTexts(String shopcat);
    }
}