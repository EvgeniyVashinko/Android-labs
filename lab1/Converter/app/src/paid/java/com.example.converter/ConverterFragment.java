package com.example.converter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class ConverterFragment extends Fragment {
    ConverterViewModel cViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cViewModel = new ViewModelProvider(requireActivity()).get(ConverterViewModel.class);
        View view = inflater.inflate(R.layout.fragment_converter, container, false);

        EditText tvIn = (EditText) view.findViewById(R.id.textViewIn);
        EditText tvOut = (EditText) view.findViewById(R.id.textViewOut);
        tvIn.setInputType(InputType.TYPE_NULL);
        tvOut.setInputType(InputType.TYPE_NULL);

        setSpinners(view, cViewModel);
        setButtons(view, cViewModel);

        cViewModel.getDataIn().observe(getViewLifecycleOwner(), val -> { tvIn.setText(val); });
        cViewModel.getDataOut().observe(getViewLifecycleOwner(), val -> { tvOut.setText(val); });
        return view;
    }

    public void setSpinners(View view, ConverterViewModel converterViewModel){
        Spinner spCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> cAdapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, cViewModel.getCategoriesNames());
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(cAdapter);

        Spinner spFrom = (Spinner) view.findViewById(R.id.spinnerFrom);
        Spinner spTo = (Spinner) view.findViewById(R.id.spinnerTo);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                converterViewModel.setCurrentCategory(name);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        spFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                converterViewModel.setCurrentConverterFrom(name);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        spTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                converterViewModel.setCurrentConverterTo(name);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        converterViewModel.getCurrentCategory().observe(getViewLifecycleOwner(), val->{
            ArrayAdapter<String> adapt = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, cViewModel.getCategoriesByName(val));
            adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFrom.setAdapter(adapt);
            spFrom.setSelection(converterViewModel.getCategoriesByName(converterViewModel.getCurrentCategory().getValue()).indexOf(converterViewModel.getCurrentConverterFrom().getValue()));
            spTo.setAdapter(adapt);
            spTo.setSelection(converterViewModel.getCategoriesByName(converterViewModel.getCurrentCategory().getValue()).indexOf(converterViewModel.getCurrentConverterTo().getValue()));
        });

        converterViewModel.getCurrentConverterFrom().observe(getViewLifecycleOwner(), val->{
            spFrom.setSelection(converterViewModel.getCategoriesByName(converterViewModel.getCurrentCategory().getValue()).indexOf(val));
        });

        converterViewModel.getCurrentConverterTo().observe(getViewLifecycleOwner(), val->{
            spTo.setSelection(converterViewModel.getCategoriesByName(converterViewModel.getCurrentCategory().getValue()).indexOf(val));
        });
    }

    public void setButtons(View view, ConverterViewModel converterViewModel){
        Button changeButton = (Button) view.findViewById(R.id.change);
        Button copyFirst = (Button) view.findViewById(R.id.copyFirst);
        Button copySecond = (Button) view.findViewById(R.id.copySecond);

        changeButton.setOnClickListener(view1 -> {
            converterViewModel.setDataIn(converterViewModel.getDataOut().getValue());
        });

        copyFirst.setOnClickListener(view1 -> {
            CopyValue(converterViewModel);
        });

        copySecond.setOnClickListener(view1 -> {
            CopyValue(converterViewModel);
        });
    }

    private void CopyValue(ConverterViewModel converterViewModel){
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", converterViewModel.getDataIn().getValue());
        clipboard.setPrimaryClip(clip);
        Toast toast = Toast.makeText(requireContext().getApplicationContext(), "Successfully copied",Toast.LENGTH_LONG);
        toast.show();
    }
}