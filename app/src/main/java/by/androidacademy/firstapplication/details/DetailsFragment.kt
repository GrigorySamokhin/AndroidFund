package by.androidacademy.firstapplication.details

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import by.androidacademy.firstapplication.R
import by.androidacademy.firstapplication.magicadditions.DownloadService
import by.androidacademy.firstapplication.data.Movie
import by.androidacademy.firstapplication.dependency.Dependencies
import by.androidacademy.firstapplication.magicadditions.DownloadService.Companion.startService
import coil.api.load
import kotlinx.android.synthetic.main.fragment_details.*
import java.lang.IllegalArgumentException

private const val ARGS_MOVIE = "ARGS_MOVIE"
private const val PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
private const val PERMISSIONS_REQUEST_CODE = 1

class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var movie: Movie
    private val isPermissionGranted: Boolean
        get() = ContextCompat.checkSelfPermission(requireActivity(), PERMISSION) ==
                PackageManager.PERMISSION_GRANTED

    companion object {

        fun newInstance(movie: Movie): DetailsFragment {
            val fragment = DetailsFragment()
            val bundle = Bundle(1)
            bundle.putParcelable(ARGS_MOVIE, movie)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = arguments?.getParcelable(ARGS_MOVIE)
            ?: throw IllegalArgumentException("Missing movie argument")
        viewModel = ViewModelProviders.of(
            this,
            DetailsViewModelFactory(
                Dependencies.moviesRepository,
                requireContext(),
                movie
            )
        ).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.openTrailerUrl.observe(
            viewLifecycleOwner,
            Observer { trailerUrl -> openMovieTrailer(trailerUrl) }
        )
        viewModel.error.observe(
            viewLifecycleOwner,
            Observer { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )

        details_iv_back.load(movie.backdropUrl)
        details_iv_image.load(movie.posterUrl)
        details_tv_title.text = movie.title
        details_tv_released_date.text = movie.releaseDate
        details_tv_overview_text.text = movie.overview
        details_btn_trailer.setOnClickListener {
            viewModel.onTrailerButtonClicked()
        }
        details_fab_poster_download.setOnClickListener { downloadPoster() }

        val movieButton: Button = view.findViewById(R.id.details_btn_trailer)
        movieButton.setOnClickListener {
            viewModel.onTrailerButtonClicked()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the contacts-related task you need to do.
                Log.d("DetailsFragment", "onRequestPermissionsResult # Permission granted")
                startDownloadService()
            } else {
                // permission denied, boo! Disable the functionality that depends on this permission.
                Log.d("DetailsFragment", "onRequestPermissionsResult # Permission denied")
            }
        }
    }

    private fun downloadPoster() {
        if (isPermissionGranted) {
            startDownloadService()
        } else {
            requestPermission()
        }
    }
    private fun startDownloadService() {
        Log.d("DetailsFragment", "startDownloadService")
        DownloadService.startService(requireContext(), movie.posterUrl)
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), PERMISSION)) {
            showExplainingRationaleDialog()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(PERMISSION), PERMISSIONS_REQUEST_CODE)
        }
    }
    private fun showExplainingRationaleDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_message)
            .setPositiveButton(R.string.dialog_ok) { _, _ ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(PERMISSION),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            .create()
            .show()
    }

    private fun openMovieTrailer(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


}