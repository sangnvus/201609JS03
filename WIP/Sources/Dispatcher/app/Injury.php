<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Injury extends Model
{
    //
    protected $table = 'injuries';

    public function instruction(){
    	return $this->hasMany('App\Instruction')->orderBy('step');
    }

    public function learningInstruction(){
    	return $this->hasMany('App\LearningInstruction')->orderBy('step');
    }

    public function question(){
    	return $this->hasMany('App\Question');
    }
}
 