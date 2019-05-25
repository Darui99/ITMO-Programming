split(null, K, null, null) :- !.
split(tree(Left, Right, Key, Val, Pr), K, ResLeft, tree(Nleft, Right, Key, Val, Pr)) :- Key > K, split(Left, K, ResLeft, Nleft).
split(tree(Left, Right, Key, Val, Pr), K, tree(Left, Nright, Key, Val, Pr), ResRight) :- Key =< K, split(Right, K, Nright, ResRight).

merge(null, R, R) :- !.
merge(L, null, L) :- !.
merge(tree(Lleft, Lright, Lkey, Lval, Lpr), tree(Rleft, Rright, Rkey, Rval, Rpr), tree(Lleft, Nright, Lkey, Lval, Lpr)) :- Lpr > Rpr, merge(Lright, tree(Rleft, Rright, Rkey, Rval, Rpr), Nright).
merge(tree(Lleft, Lright, Lkey, Lval, Lpr), tree(Rleft, Rright, Rkey, Rval, Rpr), tree(Nleft, Rright, Rkey, Rval, Rpr)) :- Lpr =< Rpr, merge(tree(Lleft, Lright, Lkey, Lval, Lpr), Rleft, Nleft).

insert(Root, tree(null, null, Key, Val, Pr), Res) :- split(Root, Key, Nleft, Nright), merge(Nleft, tree(null, null, Key, Val, Pr), ResLeft), merge(ResLeft, Nright, Res).
erase(Root, Key, Res) :- split(Root, Key, Nleft, Nright), K1 is Key - 1, split(Nleft, K1, ResLeft, Trash), merge(ResLeft, Nright, Res).

map_get(tree(Left, Right, Key, Val, Pr), Key, Val).
map_get(tree(Left, Right, Key, Val, Pr), Fkey, Res) :- Fkey < Key, map_get(Left, Fkey, Res); Fkey > Key, map_get(Right, Fkey, Res).

tree_build([], null).
tree_build([(Key, Val) | Tail], Res) :- rand_int(1000000000, Pr), tree_build(Tail, TreeMap), insert(TreeMap, tree(null, null, Key, Val, Pr), Res).

map_put(Root, Key, Val, Res) :- rand_int(1000000000, Pr), erase(Root, Key, TreeWK), insert(TreeWK, tree(null, null, Key, Val, Pr), Res).
map_remove(Root, Key, Res) :- erase(Root, Key, Res).

map_replace(Root, Key, Val, Res) :- map_get(Root, Key, _), map_put(Root, Key, Val, Res).
map_replace(Root, Key, Val, Root) :- \+map_get(Root, Key, _).

get_max_key(tree(Left, null, Key, Val, Pr), Key) :- !.
get_max_key(tree(Left, Right, Key, Val, Pr), Res) :- get_max_key(Right, Res).
map_floorKey(Root, Key, Res) :- split(Root, Key, Left, Right), get_max_key(Left, Res).