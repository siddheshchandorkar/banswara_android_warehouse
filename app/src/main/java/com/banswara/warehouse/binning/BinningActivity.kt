package com.banswara.warehouse.binning

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.dashboard.RowFilesViewModel
import com.banswara.warehouse.database.WareHouseDB
import com.banswara.warehouse.databinding.ActivityBinningBinding
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.BinningModel
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.CaptureManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random


class BinningActivity : AppCompatActivity() {
	
	private lateinit var viewModel: BinningViewModel
	private lateinit var binding: ActivityBinningBinding
	private var beepManager: BeepManager? = null
	private var capture: CaptureManager? = null


//	private var successLauncher = registerForActivityResult<Intent, ActivityResult>(
//		ActivityResultContracts.StartActivityForResult()
//	) { result: ActivityResult? ->
//		startActivity(Intent(this, DashboardActivity::class.java))
//		finish()
//
//	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_binning)
		
		intent?.let {
			if (it.hasExtra(KEY_FILE_NAME)) {
				val fileName = it.getStringExtra(KEY_FILE_NAME) ?: ""
			}
		}
		viewModel = BinningViewModel(application)
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		supportActionBar?.let {
			if (supportActionBar == null) {
				setSupportActionBar(binding.toolbar)
				it.setDisplayHomeAsUpEnabled(true)
				it.setDisplayShowHomeEnabled(true)
				it.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back))
			}
		}
		beepManager = BeepManager(this)
		
//		if (checkPermission()) {
//			val formats: Collection<BarcodeFormat> =
//				listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
//			binding.barcode.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
//			binding.barcode.initializeFromIntent(intent)
//			binding.barcode.decodeContinuous {
//				Toast.makeText(this, it.text, Toast.LENGTH_LONG).show()
//				beepManager?.playBeepSoundAndVibrate()
//			}
//		} else {
//			requestPermission()
//		}
		
		
		capture = CaptureManager(this, binding.zxingBarcodeScanner)
		capture?.initializeFromIntent(intent, savedInstanceState)
		capture?.setShowMissingCameraPermissionDialog(false)
		capture?.decode()
		changeLaserVisibility()
		val list = arrayListOf<BaseRowModel>()
		
		binding.zxingBarcodeScanner.decodeContinuous {
			Toast.makeText(this,it.text, Toast.LENGTH_LONG).show()
//			beepManager?.playBeepSoundAndVibrate()
			list.add(RowBinningViewModel(BinningModel(it.text)))
			viewModel.challanListLiveData.value = (list)
			
		}
	}
	private fun changeMaskColor() {
		val rnd = Random()
		val color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
		binding.zxingBarcodeScanner.viewFinder.setMaskColor(color)
	}
	
	private fun changeLaserVisibility() {
		binding.zxingBarcodeScanner.viewFinder.setLaserVisibility(true)
	}
	
	override fun onResume() {
		super.onResume()
		capture?.onResume()
	}
	
	override fun onPause() {
		super.onPause()
		capture?.onPause()
	}
	
	override fun onDestroy() {
		super.onDestroy()
		capture?.onDestroy()
	}
	
	
	override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
		return binding.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
	}
	
	
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String?>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		capture?.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				//TODO Siddhesh save details in db
				finish()
				
			}
		}
		return super.onOptionsItemSelected(item)
	}
	
	
	companion object {
		const val KEY_FILE_NAME = "file_name"
		const val PERMISSION_REQUEST_CODE = 200
		
	}
}