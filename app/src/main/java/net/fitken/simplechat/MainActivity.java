package net.fitken.simplechat;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.fitken.simplechat.databinding.ItemChatMeBinding;
import net.fitken.simplechat.databinding.ItemChatOtherBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button mBtnSend;
    private MessageObject mMessage;
    private EditText mEtMessage;
    private RecyclerView mRvMessages;
    private List<MessageObject> mListMessage;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        mBtnSend = (Button) findViewById(R.id.btn_send);
        mEtMessage = (EditText) findViewById(R.id.et_message);
        mRvMessages = (RecyclerView) findViewById(R.id.main_rv_chat);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    mMessage.setUserId(user.getUid());
                    mMessage.setImgProfile(user.getPhotoUrl() == null ? "" : user.getPhotoUrl().getPath());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

        mMessage = new MessageObject();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Write a message to the database

                mMessage.setMessage(mEtMessage.getText().toString());
                myRef.push().setValue(mMessage);
            }
        });


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());


                    MessageObject messageObject = dataSnapshot.getValue(MessageObject.class);
                    mListMessage.add(messageObject);


                mRvMessages.getAdapter().notifyDataSetChanged();

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.


                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        myRef.addChildEventListener(childEventListener);


        mRvMessages.setAdapter(new AbsBindingAdapter<ViewDataBinding>(new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        }) {

            int TYPE_ME = 1;
            int TYPE_OTHER = 2;

            @Override
            protected int getLayoutResourceId(int viewType) {
                if (viewType == TYPE_ME) {
                    return R.layout.item_chat_me;
                } else {
                    return R.layout.item_chat_other;
                }
            }

            @Override
            public int getItemViewType(int position) {

                return mListMessage.get(position).getUserId().equals(mMessage.getUserId()) ? TYPE_ME : TYPE_OTHER;
            }

            @Override
            public void updateBinding(ViewDataBinding binding, int position) {
                if (binding instanceof ItemChatMeBinding) {
                    ((ItemChatMeBinding) binding).setMessage(mListMessage.get(position));
                }
                if (binding instanceof ItemChatOtherBinding) {
                    ((ItemChatOtherBinding) binding).setMessage(mListMessage.get(position));
                }
            }



            @Override
            public int getItemCount() {
                return mListMessage.size();
            }
        });

        mListMessage = new ArrayList<>();
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
