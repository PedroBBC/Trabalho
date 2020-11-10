package com.example.healthyabj

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.signin.*
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)
        var num = 2


        val email =  SignInEmail.text.toString()
        val nome = SignInName.text.toString()
        val password = SignInPassword.text.toString()
        val dataNascimento = signinBirthdate.text.toString()
        val cc = signinCC.text.toString()

        auth= FirebaseAuth.getInstance()

        SignInFoto.setOnClickListener{
            Log.d("SignupActivity","Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)


        }

        signinbtVoltar.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }



        //Verifica se alguns dos campos estao vazios, se estiver manda mensagem de erro
        SignInCreateAccount.setOnClickListener {

            if(SignInEmail.text.isEmpty()){
                Toast.makeText(baseContext, "Email em Falta", Toast.LENGTH_SHORT).show()

            }

            else  if(SignInName.text.isEmpty()){
                Toast.makeText(baseContext, "Nome em falta", Toast.LENGTH_SHORT).show()

            }

            else if(SignInPassword.text.isEmpty()){
                Toast.makeText(baseContext, "Password em falta", Toast.LENGTH_SHORT).show()

            }

            else


                auth.createUserWithEmailAndPassword(SignInEmail.text.toString(), SignInPassword.text.toString())
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        //Caso nao exista nenhum erro ao criar conta, vai para a tela de login
                        // Sign in success, update UI with the signed-in user's information

                        uplaodImageToFirebaseStorage()

                        val user = auth.currentUser
                        startActivity(Intent(this,MainActivity::class.java))


                     //   SaveData()


                    } else {
                        //Caso exista algum erro ao criar conta, manda mensagem de erro
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Error 404, please try again! ", Toast.LENGTH_SHORT).show()
                    }
                }


        }


    }
    var selectedPhotoUri: Uri?= null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode,data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null)
            Log.d("SignupActivity"," photo selected")
            selectedPhotoUri=data?.data
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
        selectphoto_imageview.setImageBitmap(bitmap)
        SignInFoto.alpha =0f
    }

    private fun uplaodImageToFirebaseStorage () {
        if (selectedPhotoUri == null)return
        val filename = UUID.randomUUID().toString()
        val reffoto = FirebaseStorage.getInstance().getReference("/images/$filename")
        reffoto.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("SignupActivity","Successfully uploaded image: ${it.metadata?.path}")
                reffoto.downloadUrl.addOnSuccessListener {
                    Log.d("SignupActivity","File Location $it")
                    saveUserToFirabaseDatabase()
                }
            }


    }


    private fun saveUserToFirabaseDatabase(){
        val database = Firebase
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseFirestore.getInstance()
        //val users = User.User(SignInEmail.text.toString(),SignInPassword.text.toString(),SignInName.text.toString())
        val users = User.User (uid.toString(), SignInEmail.text.toString(),SignInName.text.toString() ,SignInPassword.text.toString() )



        ref.collection("User")
            .add(users)
            .addOnSuccessListener {  }
            .addOnFailureListener{}







//        ref.setValue(users)
//            .addOnSuccessListener {
//                Log.d("SignupActivity","Finaly we saved the user to firebase ")
//            }

        // ref.child("users").setValue(users)
        // ref.child("/Users/$uid").setValue(users)
        //ref.child("/Users/$uid").setValue(user)
        //database.child("users").child(userId).setValue(user)
    }





}












