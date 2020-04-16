package com.bengelhaupt.biometricauthentication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bengelhaupt.biometricauthentication.direct.Interaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val interaction =
            Interaction(
                this,
                ::print
            )

        interaction.start()

        enroll.setOnClickListener {
            interaction.enroll()
        }

        authenticate.setOnClickListener {
            interaction.authenticate()
        }

        fake.setOnClickListener {
            interaction.fake()
        }
    }

    private fun print(string: String) {
        runOnUiThread {
            console.append(string)
            console.append("\n")
            scroll.fullScroll(View.FOCUS_DOWN)
        }
    }
}
