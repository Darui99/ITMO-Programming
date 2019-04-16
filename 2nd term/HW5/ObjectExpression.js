const Operations = (function(){
    function AbstractOperation(...arguments) {
        this.args = arguments;
    }

    AbstractOperation.prototype.evaluate = function(x, y, z) {
        let results = [];
        for (let i = 0; i < this.args.length; i++) {
            results.push(this.args[i].evaluate(x, y, z));
        }
        return this.calc(results);
    };

    AbstractOperation.prototype.toString = function () {
        let res = "";
        for (let i = 0; i < this.args.length; i++) {
            res += this.args[i].toString() + " ";
        }
        return res + this.operationString;
    };

    function add(arg1, arg2) {
        AbstractOperation.call(this, arg1, arg2);
        this.calc = (args) => (args[0] + args[1]);
        this.operationString = "+";
        this.diff = (dt) => (new Add(arg1.diff(dt), arg2.diff(dt)));
        this.simplify = function () {
            arg1 = arg1.simplify();
            arg2 = arg2.simplify();
            if (isConst(arg1) && isConst(arg2)) {
                return new Const(arg1.value + arg2.value);
            }
            if (arg1.value === 0) {
                return arg2;
            }
            if (arg2.value === 0) {
                return arg1;
            }
            return new Add(arg1, arg2);
        }
    }
    add.prototype = Object.create(AbstractOperation.prototype);

    function subtract(arg1, arg2) {
        AbstractOperation.call(this, arg1, arg2);
        this.calc = (args) => (args[0] - args[1]);
        this.operationString = "-";
        this.diff = (dt) => (new Subtract(arg1.diff(dt), arg2.diff(dt)));
        this.simplify = function () {
            arg1 = arg1.simplify();
            arg2 = arg2.simplify();
            if (isConst(arg1) && isConst(arg2)) {
                return new Const(arg1.value - arg2.value);
            }
            if (arg1.value === 0) {
                return new Negate(arg2).simplify();
            }
            if (arg2.value === 0) {
                return arg1;
            }
            return new Subtract(arg1, arg2);
        }
    }
    subtract.prototype = Object.create(AbstractOperation.prototype);

    function multiply(arg1, arg2) {
        AbstractOperation.call(this, arg1, arg2);
        this.calc = (args) => (args[0] * args[1]);
        this.operationString = "*";
        this.diff = (dt) => (new Add(new Multiply(arg1.diff(dt), arg2), new Multiply(arg2.diff(dt), arg1)));
        this.simplify = function () {
            arg1 = arg1.simplify();
            arg2 = arg2.simplify();
            if (isConst(arg1) && isConst(arg2)) {
                return new Const(arg1.value * arg2.value);
            }
            if (arg1.value === 1) {
                return arg2;
            }
            if (arg2.value === 1) {
                return arg1;
            }
            if (arg1.value === 0 || arg2.value === 0) {
                return Zero;
            }
            return new Multiply(arg1, arg2);
        }
    }
    multiply.prototype = Object.create(AbstractOperation.prototype);

    function divide(arg1, arg2) {
        AbstractOperation.call(this, arg1, arg2);
        this.calc = (args) => (args[0] / args[1]);
        this.operationString = "/";
        this.diff = function (dt) {
            return new Divide(new Subtract(new Multiply(arg1.diff(dt), arg2), new Multiply(arg2.diff(dt), arg1)), new Multiply(arg2, arg2));
        };

        this.simplify = function () {
            arg1 = arg1.simplify();
            arg2 = arg2.simplify();
            if (isConst(arg1) && isConst(arg2)) {
                return new Const(arg1.value / arg2.value);
            }
            if (arg2.value === 1 || arg1.value === 0) {
                return arg1;
            }
            return new Divide(arg1, arg2);
        }
    }
    divide.prototype = Object.create(AbstractOperation.prototype);

    function negate(arg) {
        AbstractOperation.call(this, arg);
        this.calc = (args) => (-args[0]);
        this.operationString = "negate";
        this.diff = (dt) => (new Negate(arg.diff(dt)));
        this.simplify = function () {
            if (arg.operationString === "negate") {
                return arg.arg.simplify();
            }
            arg = arg.simplify();
            if (isConst(arg)) {
                return new Const(-arg.value);
            }
            return new Negate(arg);
        }
    }
    negate.prototype = Object.create(AbstractOperation.prototype);
    
    function atan(arg) {
        AbstractOperation.call(this, arg);
        this.calc = (args) => (Math.atan(args[0]));
        this.operationString = "atan";
        this.diff = function (dt) {
            return new Divide(arg.diff(dt), new Add(new Multiply(arg, arg), One));
        };
        this.simplify = function () {
            arg = arg.simplify();
            if (isConst(arg)) {
                return new Const(Math.atan(arg.value));
            }
            return new ArcTan(arg);
        }
    }
    atan.prototype = Object.create(AbstractOperation.prototype);

    function atan2(arg1, arg2) {
        AbstractOperation.call(this, arg1, arg2);
        this.calc = (args) => (Math.atan2(args[0], args[1]));
        this.operationString = "atan2";
        this.diff = function (dt) {
            return new Divide(new Subtract(new Multiply(arg1.diff(dt), arg2), new Multiply(arg1, arg2.diff(dt))), new Add(new Multiply(arg1, arg1), new Multiply(arg2, arg2)));
        };
        this.simplify = function () {
            arg1 = arg1.simplify();
            arg2 = arg2.simplify();
            if (isConst(arg1) && isConst(arg2)) {
                return new Const(Math.atan2(arg1.value, arg2.value));
            }
            return new ArcTan2(arg1, arg2);
        }
    }
    atan2.prototype = Object.create(AbstractOperation.prototype);

    return {
        add: add,
        subtract: subtract,
        multiply: multiply,
        divide: divide,
        negate: negate,
        atan: atan,
        atan2: atan2
    }
})();

const Operands = (function() {
    function AbstractOperand(arg) {
        this.value = arg;
    }

    AbstractOperand.prototype.evaluate = function (x, y, z) {
        switch (this.value) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
            default:
                return this.value;
        }
    };

    AbstractOperand.prototype.toString = function () {
        switch (this.value) {
            case "x":
            case "y":
            case "z":
                return this.value;
            default:
                return this.value.toString();
        }
    };

    AbstractOperand.prototype.simplify = function () {
        switch (this.value) {
            case "x":
            case "y":
            case "z":
                return new Variable(this.value);
            default:
                return new Const(this.value);
        }
    };

    function cnst(arg) {
        AbstractOperand.call(this, arg);
        this.diff = () => Zero;
    }
    cnst.prototype = Object.create(AbstractOperand.prototype);

    function variable(arg) {
        AbstractOperand.call(this, arg);
        this.diff = function (dt) {
            if (this.value === dt) {
                return One;
            } else {
                return Zero;
            }
        }
    }
    variable.prototype = Object.create(AbstractOperand.prototype);

    return {
        cnst: cnst,
        variable: variable
    }
})();

let Const = Operands.cnst;
let Variable = Operands.variable;
let Add = Operations.add;
let Subtract = Operations.subtract;
let Multiply = Operations.multiply;
let Divide = Operations.divide;
let Negate = Operations.negate;
let ArcTan = Operations.atan;
let ArcTan2 = Operations.atan2;
let One = new Const(1);
let Zero = new Const(0);

function isConst(arg) {
    return !isNaN(arg.value);
}

//------------------------------------------------------------------------

function isDigit(arg) {
    switch (arg) {
        case "0":
        case "1":
        case "2":
        case "3":
        case "4":
        case "5":
        case "6":
        case "7":
        case "8":
        case "9":
            return true;
        default:
            return false;
    }
}

function isNumber(arg) {
    let res = (isDigit(arg[0]) || arg[0] === '-');
    for (let i = 1; i < arg.length; i++) {
        res = res && isDigit(arg[i]);
    }
    return res;
}

function getOperation(arg) {
    switch (arg) {
        case "+":
            return Add;
        case "-":
            return Subtract;
        case "*":
            return Multiply;
        case "/":
            return Divide;
        case "negate":
            return Negate;
        case "atan":
            return ArcTan;
        case "atan2":
            return ArcTan2;
        default:
            return false;
    }
}

function getNumberOfArguments(arg) {
    switch (arg) {
        case Add:
        case Subtract:
        case Multiply:
        case Divide:
        case ArcTan2:
            return 2;
        case Negate:
        case ArcTan:
            return 1;
    }
}

function parse(s) {
    let arr = s.split(" ").filter((t) => (t.length > 0));
    let reducer = function (stack, now) {
        let cur = getOperation(now);
        if (cur !== false) {
            let needArg = getNumberOfArguments(cur);
            let curOpsArgs = [];
            for (let i = stack.length - needArg; i < stack.length; i++) {
                curOpsArgs.push(stack[i]);
            }
            while (needArg > 0) {
                stack.pop();
                needArg--;
            }
            stack.push(new cur(...curOpsArgs));
        } else {
            if (isNumber(now)) {
                stack.push(new Const(parseInt(now)));
            } else {
                switch (now) {
                    case "x":
                    case "y":
                    case "z":
                        stack.push(new Variable(now));
                        break;
                }
            }
        }
        return stack;
    };
    return arr.reduce(reducer, [])[0];
}