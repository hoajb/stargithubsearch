package vn.hoanguyen.stargithubsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.hoanguyen.stargithubsearch.ui.FragmentSearchUser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment()
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.rootLayout,
                FragmentSearchUser.newInstance(),
                "FragmentSearchUser"
            )
            .commit()
    }
}