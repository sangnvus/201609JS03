<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Ambulance extends Model
{
    //
    protected $table = 'ambulances';

    public function user(){
    	return $this->belongsTo('App\User');
    }

}
