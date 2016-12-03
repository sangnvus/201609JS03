<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\User;

use App\Role;

use App\Center;

use App\Ambulance;

class UserController extends Controller
{
	// Show view 'list of user'
	public function showListUserView(){
		$users = User::whereNull('isDeleted')
						->orWhere('isDeleted', '<>', 1)
						->paginate(5);
		return view('admin.user.listuser', ['users' => $users]);
	}

	// Show view 'list of user'
	public function showListUserViewByFilter($filterUser){

		$users = new User;
		if ($filterUser == 0) {
			$users = User::whereNull('isDeleted')
						->orWhere('isDeleted', '<>', 1)
						->paginate(5);
			return view('admin.user.listuser', ['users' => $users, 'filterUser' => $filterUser]);
		} else{
			//$users = User::where('role_id', '=', $filterUser)->paginate(5);
			$users = User::whereNull('isDeleted')
						->orWhere('isDeleted', '<>', 1)
						->where('role_id', '=', $filterUser)
						->paginate(5);

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
		$role = $request->role;
		// validate control data
		$this->validate($request, 
			[
				'name' => 'required|max:255',
				'phone' => 'required|numeric',
				'email' => 'required|email|max:255',
				'address' => 'required|max:255',
				'password' => 'required|max:255'
			],
			[
				'name.required' => 'Bạn phải nhập tên người dùng.',
				'name.max' => 'Tên người dùng phải nhỏ hơn 255 kí tự.',
				'phone.required' => 'Bạn phải nhập số điện thoại.',
				'phone.numeric' => 'Số điện thoại chỉ chứa số, không có dấu cách.',
				'email.required' => 'Bạn phải nhập email.',
				'email.email' => 'Bạn nhập sai định dạng email.',
				'email.max' => 'Email phải nhỏ hơn 255 kí tự.',
				'address.required' => 'Bạn phải nhập địa chỉ.',
				'address.max' => 'Địa chỉ phải nhỏ hơn 255 kí tự.',
				'password.required' => 'Bạn phải nhập mật khẩu.',
				'password.max' => 'Mật khẩu phải nhỏ hơn 255 kí tự.'
			]);

		if($role == '4') {
			$this->validate($request, 
			[
				'team' => 'required|max:255',
			],
			[
				'team.required' => 'Bạn phải nhập tên đội cho đội cứu thương.',
				'team.max' => 'Tên đội phải nhỏ hơn 255 kí tự.',
			]);
		}

		// Init object user 
		$user = User::find($id);

		// Assign user value
		$user->name = $request->name;
		$user->role_id = $role;
		if(!IsNullOrEmptyString($request->center)){
			$user->center_id = $request->center;
		}
		$user->phone = $request->phone;
		$user->email = $request->email;
		$user->address = $request->address;
		$user->password = bcrypt($request->password);
		
		//Save user to db
		$user->save();	

		// Save ambulance user if role is ambulance
		if ($role == '4') { // role = 4 => ambulance
			$ambulance = $user->ambulance;
			if($ambulance == null) {
				$ambulance = new Ambulance;
				$ambulance->user_id = $id;
			}
			$ambulance->team = $request->team;
			$ambulance->isDeleted = False;
			$ambulance->save();
		} else {
			$ambulance = $user->ambulance;
			$ambulance->isDeleted = True;
			$ambulance->save();	
		}

		if($role == 4) {
			// Return list ambulance to client and push to firebase
			$ambulances = Ambulance::all();
			return redirect('admin/user/edituser/'.$id)->with('noti', 'Đã sửa người dùng thành công')
													->with('ambulances', $ambulances);
		} else {
			return redirect('admin/user/edituser/'.$id)->with('noti', 'Đã sửa người dùng thành công');
		}

	}

	// Show view 'add user'
	public function showAddUserView(){
		$roles = Role::all();
		$centers = Center::all();
		return view('admin.user.adduser', ['roles' => $roles, 'centers' => $centers]);
	}

	// add user.
	public function addUser(Request $request){
		$role = $request->role;
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
				'address.required' => 'Bạn phải nhập địa chỉ.',
				'address.max' => 'Địa chỉ phải nhỏ hơn 255 kí tự.',
				'username.required' => 'Bạn phải nhập tên đăng nhập.',
				'username.unique' => 'Đã tồn tại tên đăng nhập.',
				'username.max' => 'Tên đăng nhập phải nhỏ hơn 45 kí tự.',
				'password.required' => 'Bạn phải nhập mật khẩu.',
				'password.max' => 'Mật khẩu phải nhỏ hơn 45 kí tự.'
			]);

		if($role == '4') {
			$this->validate($request, 
			[
				'team' => 'required|max:255',
			],
			[
				'team.required' => 'Bạn phải nhập tên đội cho đội cứu thương.',
				'team.max' => 'Tên đội phải nhỏ hơn 255 kí tự.',
			]);
		}


		// Init object user 
		$user = new User;

		// Assign user value
		$user->name = $request->name;
		$user->role_id = $role;
		if(!IsNullOrEmptyString($request->center)){
			$user->center_id = $request->center;
		}
		$user->phone = $request->phone;
		$user->email = $request->email;
		$user->address = $request->address;
		$user->username = $request->username;
		$user->password = bcrypt($request->password);
		
		//Save user to db
		$user->save();

		// Save ambulance user if role is ambulance
		if ($role == '4') { // role = 4 => ambulance
			$ambulance = new Ambulance;
			$ambulance->user_id = $user->id;
			$ambulance->team = $request->team;
			$ambulance->save();
		}

		if($role == 4) {
			// Return list ambulance to client and push to firebase
			$ambulances = Ambulance::all();
			return redirect('admin/user/listuser')->with('noti', 'Đã thêm một người dùng mới.')
													->with('ambulances', $ambulances);
		} else {
			return redirect('admin/user/listuser')->with('noti', 'Đã thêm một người dùng mới.');
		}
		
		
	}

	// Delete user
	public function deleteUser($id){
		// Find user
		$user = User::find($id);
		$username = $user->username;
		$user->isDeleted = True;
		if($user->ambulance != null) {
			$ambulance = $user->ambulance;
			$ambulance->isDeleted = True;
			$ambulance->save();
		}
		$user->save();

		$role = $user->role_id;
		if($role == 4) {
			// Return list ambulance to client and push to firebase
			$ambulances = Ambulance::all();
			return redirect('admin/user/listuser')->with('noti', 'Xóa thành công người dùng '.$username)
													->with('ambulances', $ambulances);
		} else {
			return redirect('admin/user/listuser')->with('noti', 'Xóa thành công người dùng '.$username);
		}

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
