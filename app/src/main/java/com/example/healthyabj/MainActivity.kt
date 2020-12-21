package com.example.healthyabj

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.example.healthyabj.MEDICOS.Medicos_Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.perfiluser.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       auth= FirebaseAuth.getInstance()



//mb nb
        fun recebeuseraftersignin () {
            val db = FirebaseFirestore.getInstance()
            val uid = auth.currentUser!!.email



    db.collection("User")
        .addSnapshotListener { value, e ->
            val result: StringBuffer = StringBuffer()

            for (doc in value!!) {
                if (doc.get("uid") == FirebaseAuth.getInstance().currentUser?.uid.toString()){

                    var cenas = doc.get("usertype").toString()
                    Toast.makeText(this, cenas.toString(), Toast.LENGTH_SHORT).show()
                    //Toast.makeText(this, perfiluserNome, Toast.LENGTH_SHORT).show()

                    if(cenas == "0"){

                       //Toast.makeText(this, tipo, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,HomePageActivity::class.java)
                        intent.putExtra("TipoUser",cenas)
                        startActivity(intent)
                           //startActivity(Intent(this,HomePageActivity::class.java))
                       }

                         if(cenas == "1") {
                             val intent = Intent(this,Medicos_Home::class.java)
                             intent.putExtra("TipoUser",cenas)
                             startActivity(intent)
                          //  startActivity(Intent(this,Medicos_Home::class.java))
                    }
                }
            }
        }
        }




        if (auth.currentUser != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            recebeuseraftersignin()
           // startActivity(Intent(this,HomePageActivity::class.java))
            finish()
        }

        activitymainbtSignUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))



        }




    //    activitymainbtLoginTele.setOnClickListener{
       //Ao clicar no botao de Num tele vai para outra tela
    //        startActivity(Intent(this,Loginnumtele::class.java))

      //  }



        activitymainbtLogin.setOnClickListener{
            auth.signInWithEmailAndPassword(activitymainEmail.text.toString(), activitymainPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){

                            if((activitymainEmail.text.toString().isEmpty()) || (activitymainPassword.text.toString().isEmpty())){
                                Toast.makeText(baseContext, "Por favor, insira dados! ", Toast.LENGTH_SHORT).show()
                            }

                    else   {

                        // Se consegui logar com sucesso ele muda de pagina
                        // Sign in success, update UI with the signed-in user's information
                                recebeuseraftersignin()
                        finish()
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        //Se nao conseguir logar com sucesso, manda uma mensagem de erro
                        Toast.makeText(baseContext, "Credenciais Erradas", Toast.LENGTH_SHORT).show()
                        updateUI(null)

                    }


                }

        }

        activitymainForgotPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Insira o email")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password,null)
            val username = view.findViewById<EditText>(R.id.et_usermame)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _->
                forgotPassword(username)

            })
            builder.setNegativeButton("Fechar", DialogInterface.OnClickListener { _, _->  })
            builder.show()
        }
    }

    private  fun forgotPassword(username:EditText){
        if (username.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
            return
        }
        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                  Toast.makeText(this, "Email sent.",Toast.LENGTH_LONG).show()
                }

            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI (currentUser : FirebaseUser?){


    }

}
