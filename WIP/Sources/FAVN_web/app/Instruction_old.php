<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Instruction extends Model
{
    //
    protected $table = 'Instructions';

    public function injury(){
    	return $this->belongsTo('App\Injury');
    }
}
