<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Injury extends Model
{
    //
    protected $table = 'Injury';
    protected $primaryKey = 'injury_id';

    public function instruction(){
    	return $this->hasMany('App\Instruction')->orderBy('step');
    }

    public function question(){
    	return $this->hasMany('App\Question');
    }
}
 