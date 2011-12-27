/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
package sk.tomsik68.permsguru;

 public enum EPermissionSystem
 {
  Permissions(new PermissionsPermissor()), 

  Server(new ServerPermissor()), 
  
  OP(new OPPermissor());
  private final Permissor perm;
  private EPermissionSystem(Permissor p) {
	  perm = p;
  }
  public Permissor getPermissor(){
	  return perm;
  }
}
