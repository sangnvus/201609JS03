<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class LearningInstruction extends Model
{
    //
    protected $table = 'learning_instructions';

    public function injury(){
    	return $this->belongsTo('App\Injury');
    }
}
