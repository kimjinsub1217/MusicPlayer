package com.example.musicplayer


import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private lateinit var mPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MusicPlayer)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = Handler(Looper.getMainLooper())

//        저작권법상 노래는 지움 res -> raw 폴더에 노래를 집어넣으면 됨
//        mPlayer = MediaPlayer.create(this, R.raw.bigbang)


        mPlayer.setOnPreparedListener {
            binding.seekBar.max = mPlayer.duration // 탐색 바 안에 음악이 몇 분짜리 인지 알려주기
            binding.playButton.setOnClickListener {

                if (!mPlayer.isPlaying) {
                    //음악 멈춤
                    mPlayer.start()
                    seekBar()
                    binding.playButton.setBackgroundResource(R.drawable.ic_baseline_pause_24)
                    Toast.makeText(this, "음악을 시작합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    //음악 재생
                    mPlayer.pause()
                    binding.playButton.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
                    Toast.makeText(this, "음악을 중지합니다.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        //구간을 클릭했을 때 내가 클릭한 구간으로 이동
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {

                if (fromUser) {
                    mPlayer.seekTo(progress)
                    seekBar()
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })


    }

    private fun seekBar() {
        binding.seekBar.progress = mPlayer.currentPosition
        val min: Int = mPlayer.duration / 60000  //분 단위
        val sec: Int = (mPlayer.duration - 60000 * min) / 1000 // 초 단위
        val min2: Int = mPlayer.currentPosition / 60000
        val sec2: Int = (mPlayer.currentPosition - 60000 * min2) / 1000
        binding.PlayTime.text = getString(R.string.playTime, min2, sec2)
        binding.totalTime.text = getString(R.string.totalTime, min, sec)

//        음악이 재생중이면
        if (mPlayer.isPlaying) {
            runnable = Runnable {
                seekBar()
            }
            handler.postDelayed(runnable, 1000)
        }
    }


    //다시 시작할 때 플레이 버튼으로 바꾸기
    override fun onStart() {
        binding.playButton.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
        Toast.makeText(this, "onStart에 들어왔습니다", Toast.LENGTH_SHORT).show()
        super.onStart()
    }

//    onPause() 에서 돌아올 때
    override fun onResume() {
        binding.playButton.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24)
        Toast.makeText(this, "onResume에 들어왔습니다", Toast.LENGTH_SHORT).show()
        super.onResume()
    }

    //    백그라운드로 가면 음악 멈춤
    override fun onStop() {
        super.onStop()

        Toast.makeText(this, "onStop에 들어왔습니다", Toast.LENGTH_SHORT).show()
        mPlayer.pause()

    }

    //    Pause() 상태에서 일시 정지
    override fun onPause() {
        super.onPause()
        mPlayer.pause()

    }

    //    메모리 해제
    override fun onDestroy() {
        super.onDestroy()
        mPlayer.release()

    }


}