let
  pkgs = import
    (builtins.fetchTarball {
      url = "https://github.com/NixOS/nixpkgs/archive/c82b46413401efa740a0b994f52e9903a4f6dcd5.tar.gz";
    })
    { localSystem = "x86_64-darwin"; };
in
pkgs.mkShell {
  buildInputs = [ pkgs.scala-cli ];
}
