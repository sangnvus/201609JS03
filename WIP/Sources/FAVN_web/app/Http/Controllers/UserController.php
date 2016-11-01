<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\User;

use App\Role;

use App\Center;

use App\AmbulanceCaptain;

class UserController extends Controller
{
	// Show view 'list of user'
	public function showListUserView(){
		$users = User::paginate(5);
		return view('admin.user.listuser', ['users' => $users]);
	}

	// Show view 'list of user'
	public function showListUserViewByFilter($filterUser){

		$users = new User;
		if ($filterUser == 0) {
			$users = User::paginate(5);
			return view('admin.user.listuser', ['users' => $users, 'filterUser' => $filterUser]);
		} else{
			$users = User::where('role_id', '=', $filterUser)->paginate(5);
			return view('admin.user.listuser', ['users' => $users, 'filterUser' => $filterUser]);
		}

	
	}

	// Show view 'edit user'
	public function showEditUserView($id){
		$user = User::find($id);
		$roles = Role::all();
		$centers = Center::all();
		return view('admin.user.edituser',['user'=>$user, 'roles' => $roles, 'centers' => $centers]);
	}

	// edit user.
	public function editUser(Request $request, $id){
		// validate control data
		$this->validate($request, 
			[
				'name' => 'required|max:45',
				'phone' => 'required|numeric',
				'email' => 'required|email|max:45',
				'address' => 'required|max:45',
				'password' => 'required|max:255'
			],
			[
				'name.required' => 'Bạn phải nhập tên người dùng.',
				'name.max' => 'Tên người dùng phải nhỏ hơn 45 kí tự.',
				'phone.required' => 'Bạn phải nhập số điện thoại.',
				'phone.numeric' => 'Số điện thoại chỉ chứa số, không có dấu cách.',
				'email.required' => 'Bạn phải nhập email.',
				'email.email' => 'Bạn nhập sai định dạng email.',
				'email.max' => 'Email phải nhỏ hơn 45 kí tự.',
				'password.required' => 'Bạn phải nhập mật khẩu.',
				'password.max' => 'Mật khẩu phải nhỏ hơn 45 kí tự.'
			]);

		// Init object user 
		$user = User::find($id);

		// Assign user value
		$user->name = $request->name;
		$user->role_id = $request->role;
		if(!IsNullOrEmptyString($request->center)){
			$user->center_id = $request->center;
		}
		if(!IsNullOrEmptyString($request->team)){
			$user->team = $request->team;
		}
		$user->phone = $request->phone;
		$user->email = $request->email;
		$user->address = $request->address;
		$user->password = bcrypt($request->password);
		
		//Save injury to db
		$user->save();

		return redirect('admin/user/edituser/'.$id)->with('noti', 'Đã sửa người dùng thành công');

	}

	// Show view 'add user'
	public function showAddUserView(){
		$roles = Role::all();
		$centers = Center::all();
		return view('admin.user.adduser', ['roles' => $roles, 'centers' => $centers]);
	}

	// add user.
	public function addUser(Request $request){
		// validate control data
		$this->validate($request, 
			[
				'name' => 'required|max:45',
				'phone' => 'required|numeric',
				'email' => 'required|email|max:45',
				'address' => 'required|max:45',
				'username' => 'required|unique:users,username|max:45',
				'password' => 'required|max:255'
			],
			[
				'name.required' => 'Bạn phải nhập tên người dùng.',
				'name.max' => 'Tên người dùng phải nhỏ hơn 45 kí tự.',
				'phone.required' => 'Bạn phải nhập số điện thoại.',
				'phone.numeric' => 'Số điện thoại chỉ chứa số, không có dấu cách.',
				'email.required' => 'Bạn phải nhập email.',
				'email.email' => 'Bạn nhập sai định dạng email.',
				'email.max' => 'Email phải nhỏ hơn 45 kí tự.',
				'username.required' => 'Bạn phải nhập tên đăng nhập.',
				'username.unique' => 'Đã tồn tại tên đăng nhập.',
				'username.max' => 'Tên đăng nhập phải nhỏ hơn 45 kí tự.',
				'password.required' => 'Bạn phải nhập mật khẩu.',
				'password.max' => 'Mật khẩu phải nhỏ hơn 45 kí tự.'
			]);


		// Init object user 
		$user = new User;

		// Assign user value
		$user->name = $request->name;
		$user->role_id = $request->role;
		if(!IsNullOrEmptyString($request->center)){
			$user->center_id = $request->center;
		}
		if(!IsNullOrEmptyString($request->team)){
			$user->team = $request->team;
		}
		$user->phone = $request->phone;
		$user->email = $request->email;
		$user->address = $request->address;
		$user->username = $request->username;
		$user->password = bcrypt($request->password);
		
		//Save injury to db
		$user->save();

		return redirect('admin/user/listuser')->with('noti', 'Đã thêm một người dùng mới.');
	}

	// Delete user
	public function deleteUser($id){
		// Find user
		$user = User::find($id);
		$username = $user->username;
		$user->delete();
		return redirect('admin/user/listuser')->with('noti', 'Xóa thành công người dùng '.$username);

	}

	// Search user
	public function search(Request $request){
		$term = $request->term;
		$keyword = '%'.$term.'%';

		$users = User::where('id', 'LIKE', $keyword)
		->orWhere('username', 'LIKE', $keyword)
		->orWhere('name', 'LIKE', $keyword)
		->orWhere('email', 'LIKE', $keyword)
		->orWhere('phone', 'LIKE', $keyword)
		->orWhere('address', 'LIKE', $keyword)->paginate(5);
		
        return view('admin.user.listuser',  ['users' => $users]);
	}

	
}
