<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Center extends Model
{
    //
    protected $table = 'Centers';

    public function user(){
    	return $this->hasMany('App\User');
    }
}
