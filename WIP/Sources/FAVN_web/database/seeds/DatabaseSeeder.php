<?php

use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        // $this->call(UsersTableSeeder::class);
        DB::table('users')->insert([
        	'username' => 'kien',
        	'password' => bcrypt('nguyenthuha'),
        	'center_id' => '1',
        	'role_id' => '1'
    	]);
    }
}
