package com.sam.messagemefragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Compose.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Compose#newInstance} factory method to
 * create an instance of this fragment.
 */

/*
Sam Painter and Praveen Surenani
InClass09
Compose.java
 */
public class Compose extends Fragment {

    private EditText message;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Compose.
     */
    // TODO: Rename and change types and number of parameters
    public static Compose newInstance(String param1, String param2) {
        Compose fragment = new Compose();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Compose() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        message = (EditText)view.findViewById(R.id.edittext);
        Button send = (Button)view.findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String id) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void sendMessage(){
        if (message.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), "You must input some message", Toast.LENGTH_LONG).show();
            return;
        }

        Message m = new Message();
        m.setAuthor(ParseUser.getCurrentUser());
        m.setMessage(message.getText().toString().trim());
        m.saveInBackground();
        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
        mListener.onFragmentInteraction("Messages");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
