<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use Illuminate\Support\Facades\Auth;

class AuthController extends Controller
{
	// Return view login
	public function showLoginView(){
		//Logout current user
		if(Auth::check()){
			Auth::logout();
		}
		return view('login');
	}

	// Handle login
	public function login(Request $request){
		$this->validate($request, [
				'username' => 'required',
				'password' => 'required|min:3|max:32'
			], [
				'username.required' => 'Chưa nhập tên đăng nhập.',
				'password.required' => 'Chưa nhập mật khẩu.',
				'password.required' => 'Mật khẩu không được nhỏ hơn 3 kí tự.',
				'password.max' => 'Mật khẩu không được lớn hơn 32 kí tự.',
				'password.min' => 'Mật khẩu không được nhỏ hơn 3 kí tự.'
			]);

		$username = $request->username;
		$password = $request->password;

	 	if (Auth::attempt(['username' => $username, 'password' => $password])){
			$role_id = Auth::user()->role_id;
			switch ($role_id) {
				case 1: // Admin
					return redirect('admin/dashboard');
				case 2: // Expert
					return redirect('expert/dashboard');
				default: // Other user
					return redirect('login')->with('noti', 'Tài khoản của bạn không có quyền đăng nhập.');
			}
		} else{
			return redirect('login')->with('noti', 'Sai tên tài khoản hoặc mật khẩu.');
	 	}
	}

	// Handle logout
	public function logout(){
		Auth::logout();
		return redirect('login');
	}

}
