@extends('expert.layout.index')

@section('content')
	<div class="content">
	    <div class="container-fluid">
	        <div class="row">

	            <!--Table of injuries-->
	            <div class="col-md-12">
	                <div class="card">

	                    <div class="header">
	                        <h4 class="title">
	                            Q & A
	                        </h4>
	                        <p class="category">Users Q & A here</p>
	                        <div class="row">
	                            <div class="col-md-3">
	                                <div class="stats">
	                                    <select class="form-control" name="cars">
	                                        <option value="all">All</option>
	                                        <option value="season">not answered</option>
	                                        <option value="hear">answered</option>
	                                    </select>
	                                </div>
	                            </div>
	                            <div class="col-md-9">
	                                <form class="form-inline pull-right">
	                                    <div class="form-group">
	                                        <input type="password" class="form-control" id="search" placeholder="Search">
	                                    </div>
	                                    <button type="submit" class="btn btn-default btn-fill">Search</button>
	                                </form>
	                            </div>
	                        </div>
	                    </div>

	                    <div class="content table-responsive table-full-width">
	                        <table class="table table-hover table-striped">
	                            <thead>
	                                <th></th>
	                                <th>ID</th>
	                                <th>User name</th>
	                                <th>Email</th>
	                                <th>Q & A title</th>
	                                <th>Q & A content</th>
	                                <th>Manipulate</th>
	                            </thead>
	                            <tbody>
	                                <tr>
	                                    <td>
	                                        <label class="checkbox">
	                                            <input type="checkbox" value="" data-toggle="checkbox">
	                                        </label>
	                                    </td>
	                                    <td>1</td>
	                                    <td style="white-space:nowrap">Hướng Nhật</td>
	                                    <td>huongnhat.net@gmail.com</td>
	                                    <td>Because they’re delicate, this can happen easily.</td>
	                                    <td>Because they’re delicate, this can happen easily.. Because they’re delicate, this can happen easily.</td>
	                                    <td class="td-actions text-right" style="white-space:nowrap">
	                                        <button type="button" rel="tooltip" title="View" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='viewq&a.html'">
	                                            <i class="fa fa-picture-o"></i>
	                                        </button>
	                                        <button type="button" rel="tooltip" title="Reply" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='answer.html'">
	                                            <i class="fa fa-reply"></i>
	                                        </button>
	                                        <button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs" onclick="DeleteRow(this)">
	                                            <i class="fa fa-times"></i>
	                                        </button>
	                                    </td>
	                                </tr>

	                                <tr>
	                                    <td>
	                                        <label class="checkbox">
	                                            <input type="checkbox" value="" data-toggle="checkbox">
	                                        </label>
	                                    </td>
	                                    <td>1</td>
	                                    <td>Hùng Già</td>
	                                    <td>hunggia@gmail.com</td>
	                                    <td>can cause pain and could damage the cornea.</td>
	                                    <td>Anything that gets in your eye, whether it’s a speck of sand or a chemical. Anything that gets in your eye, whether it’s a speck of sand or a chemical. Anything that gets in your eye, whether it’s a speck of sand or a chemical,</td>
	                                    <td class="td-actions text-right" style="white-space:nowrap">
	                                        <button type="button" rel="tooltip" title="View" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='viewq&a.html'">
	                                            <i class="fa fa-picture-o"></i>
	                                        </button>
	                                        <button type="button" rel="tooltip" title="Reply" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='answer.html'">
	                                            <i class="fa fa-reply"></i>
	                                        </button>
	                                        <button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs" onclick="DeleteRow(this)">
	                                            <i class="fa fa-times"></i>
	                                        </button>
	                                    </td>
	                                </tr>

	                                <tr>
	                                    <td>
	                                        <label class="checkbox">
	                                            <input type="checkbox" value="" data-toggle="checkbox">
	                                        </label>
	                                    </td>
	                                    <td>1</td>
	                                    <td>NTH</td>
	                                    <td>Hnlover@gmail.com</td>
	                                    <td>Sprains are often accompanied by bruising and swelling.
	                                    </td>
	                                    <td>Sprains are often accompanied by bruising and swelling. Sprains are often accompanied by bruising and swelling. Sprains are often accompanied by bruising and swelling.</td>
	                                    <td class="td-actions text-right" style="white-space:nowrap">
	                                        <button type="button" rel="tooltip" title="View" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='viewq&a.html'">
	                                            <i class="fa fa-picture-o"></i>
	                                        </button>
	                                        <button type="button" rel="tooltip" title="Reply" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='answer.html'">
	                                            <i class="fa fa-reply"></i>
	                                        </button>
	                                        <button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs" onclick="DeleteRow(this)">
	                                            <i class="fa fa-times"></i>
	                                        </button>
	                                    </td>
	                                </tr>

	                                <tr>
	                                    <td>
	                                        <label class="checkbox">
	                                            <input type="checkbox" value="" data-toggle="checkbox">
	                                        </label>
	                                    </td>
	                                    <td>1</td>
	                                    <td>other</td>
	                                    <td>other@gmail.com</td>
	                                    <td>hird-degree burns result in broken or blackened skin.</td>
	                                    <td>hird-degree burns result in broken or blackened skin. hird-degree burns result in broken or blackened skin. hird-degree burns result in broken or blackened skin.</td>
	                                    <td class="td-actions text-right" style="white-space:nowrap">
	                                        <button type="button" rel="tooltip" title="View" class="btn btn-info btn-simple btn-xs"  onclick="window.location.href='viewq&a.html'">
	                                            <i class="fa fa-picture-o"></i>
	                                        </button>
	                                        <button type="button" rel="tooltip" title="Reply" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='answer.html'">
	                                            <i class="fa fa-reply"></i>
	                                        </button>
	                                        <button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs" onclick="DeleteRow(this)">
	                                            <i class="fa fa-times"></i>
	                                        </button>
	                                    </td>
	                                </tr>

	                                <tr>
	                                    <td>
	                                        <label class="checkbox">
	                                            <input type="checkbox" value="" data-toggle="checkbox">
	                                        </label>
	                                    </td>
	                                    <td>1</td>
	                                    <td>haha</td>
	                                    <td>haha@fpt.edu.vn</td>
	                                    <td>But if the force is great, the neck, the back, and soft tissues inside the head can be injured.</td>
	                                    <td>But if the force is great, the neck, the back, and soft tissues inside the head can be injured. But if the force is great, the neck, the back, and soft tissues inside the head can be injured.</td>
	                                    <td class="td-actions text-right" style="white-space:nowrap">
	                                        <button type="button" rel="tooltip" title="View" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='viewq&a.html'">
	                                            <i class="fa fa-picture-o"></i>
	                                        </button>
	                                        <button type="button" rel="tooltip" title="Reply" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='answer.html'">
	                                            <i class="fa fa-reply"></i>
	                                        </button>
	                                        <button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs" onclick="DeleteRow(this)">
	                                            <i class="fa fa-times"></i>
	                                        </button>
	                                    </td>
	                                </tr>

	                            </tbody>
	                        </table>

	                    </div>
	                </div>
	                <!--End table injuries-->

	            </div>
	        </div>
	    </div>
	</div>
@endsection