package id.nns.nikos.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import id.nns.nikos.R
import id.nns.nikos.dashboard.DashboardFragment
import id.nns.nikos.databinding.ActivityHomeBinding
import id.nns.nikos.wallet.WalletFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(
            DashboardFragment()
        )

        setMeowBottomNav()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    private fun setMeowBottomNav() {
        binding.meowBottomNavigation.apply {
            add(MeowBottomNavigation.Model(0, R.drawable.ic_dashboard))
            add(MeowBottomNavigation.Model(1, R.drawable.ic_wallet))

            show(0)

            setOnClickMenuListener {
                when (it.id) {
                    0 -> {
                        replaceFragment(
                            DashboardFragment()
                        )
                    }
                    1 -> {
                        replaceFragment(
                            WalletFragment()
                        )
                    }
                }
            }
        }
    }

}
