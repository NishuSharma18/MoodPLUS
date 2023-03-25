package com.example.android.moodplus.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.android.moodplus.R;
import com.example.android.moodplus.activities.GlobalChatActivity;
import com.example.android.moodplus.adapter.CommunitiesAdapter;
import com.example.android.moodplus.model.Community;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CommunitiesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Community> communities;
    private ProgressBar progressBar;
    private CommunitiesAdapter communitiesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    CommunitiesAdapter.OnCommunityClickListener onCommunityClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_communities,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getView().findViewById(R.id.commActivity_recycler);
        progressBar = getView().findViewById(R.id.commActivity_progressbar);
        swipeRefreshLayout = getView().findViewById(R.id.commActivity_swip);

        communities = new ArrayList<>();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getCommunities();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        getCommunities();
//        getImages();

        onCommunityClickListener = new CommunitiesAdapter.OnCommunityClickListener() {
            @Override
            public void onCommunityClicked(int position) {
                Intent intent = new Intent(getActivity(),GlobalChatActivity.class);
                intent.putExtra("Community_name",communities.get(position).getCommName());
                startActivity(intent);
            }
        };
//        setAdapter();
    }

    private void getCommunities() {

        Community community1 = new Community("Anxiety","","Breathe in calm, breathe out anxiety");
        communities.add(community1);
        Community community2 = new Community("Stress","","Stressed out? Our community has your back");
        communities.add(community2);
        Community community4 = new Community("Fear","","In the face of fear, we find strength in community and support");
        communities.add(community4);
        Community community5 = new Community("Loneliness","","Loneliness may be a feeling, but the Loneliness Community is a community");
        communities.add(community5);
        Community community6 = new Community("Inspiration","","Inspiration fuels action, action fuels change");
        communities.add(community6);
        Community community7 = new Community("Anger","","Transform your rage into peace");
        communities.add(community7);
        Community community9 = new Community("Happiness","","If you want to live a happy life, tie it to a goal, not to people or things");
        communities.add(community9);
    }

    private void setAdapter(){
        communitiesAdapter = new CommunitiesAdapter(communities,getActivity(),onCommunityClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(communitiesAdapter);
    }

    private void getImages(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://betaapp-4dbd0.appspot.com");
        StorageReference pathReference = storageRef.child("rawimages/frustrated.png");

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Get the download URL for 'frustrated.png'
                String downloadUrl = uri.toString();
                Community communityfrust = new Community("Frustrated",downloadUrl,"Release your frustration, find your peace");
                communities.add(communityfrust);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception exception) {
                // Handle any errors
            }
        });
        StorageReference pathReference1 = storageRef.child("rawimages/affection.png");

        pathReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Get the download URL for 'frustrated.png'
                String downloadUrl = uri.toString();
                Community communityfrust = new Community("Affection",downloadUrl,"Release your frustration, find your peace");
                communities.add(communityfrust);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception exception) {
                // Handle any errors
            }
        });
        StorageReference pathReference2 = storageRef.child("rawimages/angry.png");

        pathReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Get the download URL for 'frustrated.png'
                String downloadUrl = uri.toString();
                Community communityfrust = new Community("Anger",downloadUrl,"Release your frustration, find your peace");
                communities.add(communityfrust);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception exception) {
                // Handle any errors
            }
        });

        StorageReference pathReference3 = storageRef.child("rawimages/fear.png");

        pathReference3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Get the download URL for 'frustrated.png'
                String downloadUrl = uri.toString();
                Community communityfrust = new Community("Fear",downloadUrl,"Release your frustration, find your peace");
                communities.add(communityfrust);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception exception) {
                // Handle any errors
            }
        });

        StorageReference pathReference4 = storageRef.child("rawimages/inspiration.png");

        pathReference4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Get the download URL for 'frustrated.png'
                String downloadUrl = uri.toString();
                Community communityfrust = new Community("Inspiration",downloadUrl,"Release your frustration, find your peace");
                communities.add(communityfrust);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception exception) {
                // Handle any errors
            }
        });

        StorageReference pathReference5 = storageRef.child("rawimages/loneliness.png");

        pathReference5.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Get the download URL for 'frustrated.png'
                String downloadUrl = uri.toString();
                Community communityfrust = new Community("Loneliness",downloadUrl,"Release your frustration, find your peace");
                communities.add(communityfrust);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception exception) {
                // Handle any errors
            }
        });

        StorageReference pathReference6 = storageRef.child("rawimages/stress.png");

        pathReference6.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Get the download URL for 'frustrated.png'
                String downloadUrl = uri.toString();
                Community communityfrust = new Community("Stress",downloadUrl,"Release your frustration, find your peace");
                communities.add(communityfrust);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception exception) {
                // Handle any errors
            }
        });

        setAdapter();
    }
}