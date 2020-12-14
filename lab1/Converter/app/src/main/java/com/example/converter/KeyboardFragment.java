package com.example.converter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class KeyboardFragment extends Fragment implements View.OnClickListener {

    ConverterViewModel cViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cViewModel = new ViewModelProvider(requireActivity()).get(ConverterViewModel.class);
        View view = inflater.inflate(R.layout.fragment_keyboard, container, false);

        Button button0 = (Button) view.findViewById(R.id.b_0);
        Button button1 = (Button) view.findViewById(R.id.b_1);
        Button button2 = (Button) view.findViewById(R.id.b_2);
        Button button3 = (Button) view.findViewById(R.id.b_3);
        Button button4 = (Button) view.findViewById(R.id.b_4);
        Button button5 = (Button) view.findViewById(R.id.b_5);
        Button button6 = (Button) view.findViewById(R.id.b_6);
        Button button7 = (Button) view.findViewById(R.id.b_7);
        Button button8 = (Button) view.findViewById(R.id.b_8);
        Button button9 = (Button) view.findViewById(R.id.b_9);
        Button buttonDot = (Button) view.findViewById(R.id.b_dot);
        Button buttonDel = (Button) view.findViewById(R.id.b_delete);


        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonDot.setOnClickListener(this);
        buttonDel.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        Button b = (Button)view;
        if (cViewModel.getDataIn().getValue() == null){
            cViewModel.setDataIn("");
        }
        cViewModel.setDataIn(cViewModel.getDataIn().getValue() + b.getText());
        System.out.println(b.getText());
        System.out.println(cViewModel.getDataIn().getValue());
        System.out.println("-------------");
    }
}