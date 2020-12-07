package vn.hoanguyen.stargithubsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.hoanguyen.stargithubsearch.ui.FragmentSearchUser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Only add fragment for init 1st time. Rotate will reuse/restore fragment by activity
        if (savedInstanceState == null)
            showFragment()
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.rootLayout,
                FragmentSearchUser.newInstance(),
                "FragmentSearchUser"
            )
            .commit()
    }
}