<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Caller;

class CallerResourceController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        // Declare object caller
        $caller = new Caller;

        // Assign value from request
        $caller->phone = $request->input('phone');
        $caller->latitude = $request->input('latitude');
        $caller->longitude = $request->input('longitude');
        $caller->status = $request->input('status');

        $injury_id = $request->input('injuryId');
        if(!IsNullOrEmptyString($injury_id)) {
            $caller->injury_id = $injury_id;
        }

        $symptom = $request->input('symptom');
        if(!IsNullOrEmptyString($symptom)) {
            $caller->symptom = $symptom;
        }



        // Save to db
        $caller->save();

        echo 'Gửi thông tin thành công';
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        //
    }
}
