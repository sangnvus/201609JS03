<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Center extends Model
{
    //
    protected $table = 'centers';

    public function user(){
    	return $this->hasMany('App\User');
    }
}
