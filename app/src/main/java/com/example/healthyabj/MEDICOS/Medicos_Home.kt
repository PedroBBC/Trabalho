package com.example.healthyabj.MEDICOS

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthyabj.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.medico_homepage.*

class Medicos_Home  : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.medico_homepage)

        lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()




        val user = FirebaseAuth.getInstance().currentUser
        val useremail = user?.email






        fun ReceiveDataPlace () {


            val fromId = FirebaseAuth.getInstance().uid

            db.collection("User")

                .addSnapshotListener { value, e ->

                    if (e != null) {
                        Log.w(ChatLogActivity.TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }



                    for (doc in value!!) {

                        var email = doc.get("email").toString()
                        var name = doc.get("name").toString()

                        if (email.toLowerCase() == useremail) {

                            tvmedicoshomepageEmail.setText(useremail)
                            tvmedicoshomepageNome.setText(name)


                        }
                    }
                }
        }
            ReceiveDataPlace()






























/*
        val docRef = db.collection("User").document(useremail.toString())
        docRef.get()
         .addOnSuccessListener { document ->
                if (document != null) {


                    var email = useremail
                    //var data = document.get("EmaiPaciente").toString()
                    var nome = document.get("name").toString()


                    tvmedicoshomepageNome.setText(document.get("name").toString())
                    tvmedicoshomepageEmail.setText(email)

                  }
                else
                    {

                    }

                    }


            .addOnFailureListener { exception ->

            }

 */






        medicos_home_localizacao.setOnClickListener {
        startActivity(Intent(this, MapsActivity::class.java))
        }

        medicos_homebtchat.setOnClickListener {

            val intent = Intent(this,NewMessageActivity::class.java)
            startActivity(intent)

        }
        medicos_homebtConsultas.setOnClickListener{
            val profileName=intent.getStringExtra("TipoUser")
            val intent = Intent(this, ProxConsultasActivitie::class.java)
            intent.putExtra("TipoUser",profileName)
            startActivity(intent)
        }


        fun onOptionsItemSelected(item: MenuItem): Boolean {
            item?.itemId
            R.id.menu_new_message
            val intent = Intent(this,NewMessageActivity::class.java)
            startActivity(intent)


            return super.onOptionsItemSelected(item)
        }



//        medicos_homeac_perfil.setOnClickListener {
//
//            startActivity(Intent(this, PerfilUser::class.java))
//
//
//
//
//        }

}
}
