let
  args = if builtins.currentSystem == "aarch64-darwin" then { localSystem = "x86_64-darwin"; } else { };
  pkgs = import
    (builtins.fetchTarball {
      url = "https://github.com/NixOS/nixpkgs/archive/c82b46413401efa740a0b994f52e9903a4f6dcd5.tar.gz";
    })
    args;
in
pkgs.mkShell {
  buildInputs = [ pkgs.scala-cli ];
}
