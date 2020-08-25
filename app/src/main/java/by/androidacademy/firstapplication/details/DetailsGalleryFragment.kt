package by.androidacademy.firstapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import by.androidacademy.firstapplication.details.DetailsFragmentAdapter
import by.androidacademy.firstapplication.data.Movie
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.fragment_gallary_details.*

class DetailsGalleryFragment : Fragment() {

    companion object {

        private const val ARGS_MOVIE = "ARGS_MOVIE"
        private const val ARGS_MOVIE_POSITION = "ARGS_MOVIE_POSITION"

        fun newInstance(
            movie: List<Movie>,
            position: Int
        ): DetailsGalleryFragment {
            val fragment = DetailsGalleryFragment()
            val bundle = Bundle(2)
            bundle.run {
                putParcelableArrayList(ARGS_MOVIE, ArrayList(movie))
                putInt(ARGS_MOVIE_POSITION, position)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallary_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val movies = arguments?.getParcelableArrayList<Movie>(ARGS_MOVIE) ?: throw IllegalArgumentException("Missing movie argument")
        val position = arguments?.getInt(ARGS_MOVIE_POSITION) ?: 0

        vp_pager.apply {
            adapter =
                DetailsFragmentAdapter(
                    childFragmentManager,
                    movies
                )
            currentItem = position
        }
    }


}