<?php

namespace App\Http\Middleware;

use Closure;

use Illuminate\Support\Facades\Auth;

class ExpertLoginMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        if (Auth::check()) { 
            $userLogin = Auth::user();
            if ($userLogin->role_id == 2) {
                view()->share('userLogin', $userLogin);
                return $next($request);
            }
        }
        return redirect('accessdenied');
    }
}
