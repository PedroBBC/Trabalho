package com.example.healthyabj

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.signin.*
import java.util.*

import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.minhasconsultasuser.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)
        var num = 2


        val email = SignInEmail.text.toString()
        val nome = SignInName.text.toString()
        val password = SignInPassword.text.toString()
        val dataNascimento = signinBirthdate.text.toString()
        //val cc = signinCC.text.toString()

        auth = FirebaseAuth.getInstance()

        SignInFoto.setOnClickListener {
            Log.d("SignupActivity", "Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)


        }

        signinbtVoltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

//tem q dar

        signinBirthdate.setOnClickListener {



                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)


                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                        if ((mDay < 10) && (mMonth < 10)) {

                            DataNasSign.setText("" + mYear + "-" + "0" + (mMonth + 1) + "-" + "0" + mDay)
                            var date = ("" + mYear + "-" + "0" + (mMonth + 1) + "-" + "0" + mDay)



                        } else if (mMonth < 10) {
                            DataNasSign.setText("" + mYear + "-" + "0" + (mMonth + 1) + "-" + mDay)
                            var date = ("" + mYear + "-" + "0" + (mMonth + 1) + "-" + mDay)



                        } else if (mDay < 10) {
                            DataNasSign.setText("" + mYear + "-" + (mMonth + 1) + "-" + "0" + mDay)
                            var date = ("" + mYear + "-" + (mMonth + 1) + "-" + "0" + mDay)



                        } else {
                            DataNasSign.setText("" + mYear + "-" + (mMonth + 1) + "-" + mDay)
                            var date = ("" + mYear + "-" + (mMonth + 1) + "-" + mDay)


                        }


                        //  recebedia2 (date)

                    }, year, month, day
                )
                dpd.show()



            }




        val regex = "^(?=.*?\\p{Lu})(?=.*?\\p{Ll})(?=.*?\\d)" +
                "(?=.*?[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).*$"

        fun ValidatePasds(Pass: String): Boolean {
            if(Pass.length < 6){

                ErrorPassword.text = "Password Muito Pequena! "
                return false
            }

            else if(Pass.isEmpty() == true){

                return false

            }


            else if (Pattern.compile(regex).matcher(Pass).matches() == false) {

                ErrorPassword.text = "Palavra Passe muito fraca!"
                return false
            }






            else{
                ErrorPassword.text = ""
                return true
            }


        }

        val regex2 = "@."
        fun ValidateEmail(Email: String) :Boolean {



            if (android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches() == false) {
                ErrorEmail.text = "Email Invalido, por favor, Insira outro!"
                return false
            }
            else if(Email == "" ){
                ErrorEmail.text = "Email Invalido, por favor, Insira outro!"
                return false
            }
            else{
                ErrorEmail.text = ""
                    return true
            }

        }


        //Verifica se alguns dos campos estao vazios, se estiver manda mensagem de erro
        SignInCreateAccount.setOnClickListener {

            ValidateEmail(SignInEmail.text.toString())
            ValidatePasds(SignInPassword.text.toString())

            if((ValidateEmail(SignInEmail.text.toString()) == true ) && ( ValidatePasds(SignInPassword.text.toString()) == true ))
            {




                auth.createUserWithEmailAndPassword(SignInEmail.text.toString(), SignInPassword.text.toString())
                    .addOnCompleteListener(this) {task ->
                        if (task.isSuccessful) {
                            //Caso nao exista nenhum erro ao criar conta, vai para a tela de login
                            // Sign in success, update UI with the signed-in user's information

                            uplaodImageToFirebaseStorage()

                            val user = auth.currentUser

                            // startActivity(Intent(this,MainActivity::class.java))


                            startActivity(Intent(this,MainActivity::class.java))




                            //   SaveData()


                        } else {
                            //Caso exista algum erro ao criar conta, manda mensagem de erro
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Error 404, please try again! ", Toast.LENGTH_SHORT).show()
                        }
                    }





            }

            else {

                Toast.makeText(baseContext, "Credenciais invalidas! ", Toast.LENGTH_SHORT).show()
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
        //selectphoto_imageview.rotation = 90
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
                    saveUserToFirabaseDatabase(it.toString())
                }
            }


    }




    private fun saveUserToFirabaseDatabase(profileImageUrl:String){

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseFirestore.getInstance()



var a = DataNasSign.text.toString()
        val users = User.User (uid.toString(), SignInEmail.text.toString(),SignInName.text.toString() ,SignInPassword.text.toString(),profileImageUrl,0,a,0 )

val cena = SignInEmail.text.toString()


        ref.collection("User").document(cena)
            .set(users)
            .addOnSuccessListener {  }
            .addOnFailureListener{}



    }





}












