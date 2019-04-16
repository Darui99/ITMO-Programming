const Operations = (function(){
    function AbstractOperation(...arguments) {
        this.args = arguments;
    }

    AbstractOperation.prototype.evaluate = function(x, y, z) {
        return this.calc(this.args.map(elem => elem.evaluate(x, y, z)));
    };

    AbstractOperation.prototype.toString = function () {
        let res = "";
        for (let i = 0; i < this.args.length; i++) {
            res += this.args[i].toString() + " ";
        }
        return res + this.operationString;
    };

    AbstractOperation.prototype.prefix = function () {
        let res = "(" + this.operationString;
        if (this.args.length === 0) {
            res += " ";
        }
        for (let i = 0; i < this.args.length; i++) {
            res += " " + this.args[i].prefix();
        }
        return res + ")";
    };

    AbstractOperation.prototype.postfix = function () {
        let res = "(";
        if (this.args.length === 0) {
            res += " ";
        }
        for (let i = 0; i < this.args.length; i++) {
            res += this.args[i].postfix() + " ";
        }
        return res + this.operationString + ")";
    };

    function add(arg1, arg2) {
        AbstractOperation.call(this, arg1, arg2);
        this.calc = (args) => (args[0] + args[1]);
        this.operationString = "+";
        this.diff = (dt) => (new Add(arg1.diff(dt), arg2.diff(dt)));
    }
    add.prototype = Object.create(AbstractOperation.prototype);

    function subtract(arg1, arg2) {
        AbstractOperation.call(this, arg1, arg2);
        this.calc = (args) => (args[0] - args[1]);
        this.operationString = "-";
        this.diff = (dt) => (new Subtract(arg1.diff(dt), arg2.diff(dt)));
    }
    subtract.prototype = Object.create(AbstractOperation.prototype);

    function multiply(arg1, arg2) {
        AbstractOperation.call(this, arg1, arg2);
        this.calc = (args) => (args[0] * args[1]);
        this.operationString = "*";
        this.diff = (dt) => (new Add(new Multiply(arg1.diff(dt), arg2), new Multiply(arg2.diff(dt), arg1)));
    }
    multiply.prototype = Object.create(AbstractOperation.prototype);

    function divide(arg1, arg2) {
        AbstractOperation.call(this, arg1, arg2);
        this.calc = (args) => (args[0] / args[1]);
        this.operationString = "/";
        this.diff = function (dt) {
            return new Divide(new Subtract(new Multiply(arg1.diff(dt), arg2), new Multiply(arg2.diff(dt), arg1)), new Multiply(arg2, arg2));
        };
    }
    divide.prototype = Object.create(AbstractOperation.prototype);

    function negate(arg) {
        AbstractOperation.call(this, arg);
        this.calc = (args) => (-args[0]);
        this.operationString = "negate";
        this.diff = (dt) => (new Negate(arg.diff(dt)));
    }
    negate.prototype = Object.create(AbstractOperation.prototype);

    function sumsq(...args) {
        AbstractOperation.call(this, ...args);
        this.calc = function (args) {
            let reducer = (sum, cur) => (sum + cur * cur);
            return args.reduce(reducer, 0);
        };
        this.operationString = "sumsq";
        this.diff = function (dt) {
            let dArgs = [];
            for (let i = 0; i < args.length; i++) {
                dArgs.push(new Multiply(args[i], args[i]).diff(dt));
            }
            if (dArgs.length === 0) {
                return new Sumsq(...[]);
            }
            if (dArgs.length === 1) {
                return dArgs[0];
            }
            let res = new Add(dArgs[0], dArgs[1]);
            for (let i = 2; i < dArgs.length; i++) {
                res = new Add(res, dArgs[i]);
            }
            return res;
        }
    }
    sumsq.prototype = Object.create(AbstractOperation.prototype);

    function length(...args) {
        AbstractOperation.call(this, ...args);
        this.calc = function (args) {
            let reducer = (sum, cur) => (sum + cur * cur);
            return Math.sqrt(args.reduce(reducer, 0));
        };
        this.operationString = "length";
        this.diff = function (dt) {
            if (args.length === 0) {
                return new Const(0);
            }
            return new Multiply(new Divide(One, new Multiply(new Const(2), new Length(...args))), new Sumsq(...args).diff(dt));
        }
    }
    length.prototype = Object.create(AbstractOperation.prototype);

    return {
        add: add,
        subtract: subtract,
        multiply: multiply,
        divide: divide,
        negate: negate,
        sumsq: sumsq,
        length: length
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

    AbstractOperand.prototype.prefix = AbstractOperand.prototype.toString;

    AbstractOperand.prototype.postfix = AbstractOperand.prototype.toString;

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
let Sumsq = Operations.sumsq;
let Length = Operations.length;
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
    let res = (isDigit(arg[0]) || arg[0] === '-' && arg.length > 1);
    for (let i = 1; i < arg.length; i++) {
        res = res && isDigit(arg[i]);
    }
    return res;
}

function isVariable(arg) {
    switch (arg) {
        case "x":
        case "y":
        case "z":
            return true;
        default:
            return false;
    }
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
        case "sumsq":
            return Sumsq;
        case "length":
            return Length;
        default:
            return false;
    }
}

//-----------------------------------------------------------------------------

const Exceptions = (function(){
    function AbstractException(message) {
        this.message = message;
    }
    AbstractException.prototype = Object.create(Error.prototype);

    function IBSException() {
        let message = "Expression has incorrect bracket sequence";
        AbstractException.call(this, message);
        this.name = "IncorrectBracketSequenceException";
    }
    IBSException.prototype = Object.create(AbstractException.prototype);

    function UnknownOperationException(pos) {
        let message = "Expression has unknown operation at " + pos.toString() + " symbol";
        AbstractException.call(this, message);
        this.name = "UnknownOperationException";
    }
    UnknownOperationException.prototype = Object.create(AbstractException.prototype);

    function IncorrectOperandException(pos) {
        let message = "Expression has incorrect operand at " + pos.toString() + " symbol";
        AbstractException.call(this, message);
        this.name = "IncorrectOperandException";
    }
    IncorrectOperandException.prototype = Object.create(AbstractException.prototype);

    function MissingOperandException(...args) {
        let message;
        if (args.length !== 0) {
            message = "Expected operand for operation at " + args[0].toString() + " symbol";
        } else {
            message = "Expression does not have enough operands";
        }
        AbstractException.call(this, message);
        this.name = "MissingOperandException";
    }
    MissingOperandException.prototype = Object.create(AbstractException.prototype);

    function MissingOperationException(...args) {
        let message;
        if (args.length !== 0) {
            message = "Expected operation at " + args[0].toString() + " symbol";
        } else {
            message = "Expression does not have enough operations";
        }
        AbstractException.call(this, message);
        this.name = "MissingOperationException";
    }
    MissingOperationException.prototype = Object.create(AbstractException.prototype);

    return {
        IBSException: IBSException,
        UnknownOperationException: UnknownOperationException,
        IncorrectOperandException: IncorrectOperandException,
        MissingOperandException: MissingOperandException,
        MissingOperationException: MissingOperationException
    }
})();

let IBSException = Exceptions.IBSException;
let UnknownOperationException = Exceptions.UnknownOperationException;
let IncorrectOperandException = Exceptions.IncorrectOperandException;
let MissingOperandException = Exceptions.MissingOperandException;
let MissingOperationException = Exceptions.MissingOperationException;

function getNumberOfArguments(arg) {
    switch (arg) {
        case Add:
        case Subtract:
        case Multiply:
        case Divide:
            return 2;
        case Negate:
            return 1;
        default:
            return false;
    }
}

function pair(key, value) {
    this.getKey = () => key;
    this.getValue = () => value;
}

function parsePrefix(s) {
    let stackOperations = [], stackOperands = [], stackNumberArgs = [];
    let balance = 0;

    for (let i = 0; i < s.length;) {
        while (i < s.length && s[i] === " ") {
            i++;
        }
        if (i === s.length) {
            break;
        }
        let curToken = "", startPos = i, curOperation;
        if (s[i] === '(') {
            balance++;
            startPos = i++;
            while (i < s.length && s[i] === " ") {
                i++;
            }
            if (i === s.length || s[i] === '(') {
                throw new MissingOperationException(startPos);
            }
            startPos = i;
            while (i < s.length && s[i] !== ' ' && s[i] !== '(' && s[i] !== ')') {
                curToken += s[i];
                i++;
            }
            curOperation = getOperation(curToken);
            if (curOperation === false) {
                throw new UnknownOperationException(startPos);
            }
            stackOperations.push(new pair(curOperation, startPos));
            stackNumberArgs.push(0);
            continue;
        }
        if (s[i] === ')') {
            balance--;
            if (balance < 0) {
                throw new IBSException();
            }
            let needArg = getNumberOfArguments(stackOperations[stackOperations.length - 1].getKey());
            if (needArg === false) {
                needArg = stackNumberArgs[stackNumberArgs.length - 1];
            }
            if (stackOperands.length < needArg) {
                throw new MissingOperandException(stackOperations[stackOperations.length - 1].getValue());
            }
            let curOpsArgs = [];
            for (let i = stackOperands.length - needArg; i < stackOperands.length; i++) {
                curOpsArgs.push(stackOperands[i]);
            }
            while (needArg > 0) {
                stackOperands.pop();
                needArg--;
            }
            curOperation = stackOperations[stackOperations.length - 1].getKey();
            stackOperands.push(new curOperation(...curOpsArgs));
            stackOperations.pop();
            stackNumberArgs.pop();
            stackNumberArgs[stackNumberArgs.length - 1]++;
            i++;
            continue;
        }
        while (i < s.length && s[i] !== ' ' && s[i] !== '(' && s[i] !== ')') {
            curToken += s[i];
            i++;
        }
        if (isNumber(curToken)) {
            stackOperands.push(new Const(parseInt(curToken)));
            stackNumberArgs[stackNumberArgs.length - 1]++;
        } else {
            if (isVariable(curToken)) {
                stackOperands.push(new Variable(curToken));
                stackNumberArgs[stackNumberArgs.length - 1]++;
            } else {
                throw new IncorrectOperandException(startPos);
            }
        }
    }
    if (balance !== 0) {
        throw new IBSException();
    }
    if (stackOperations.length > 0 || stackOperands.length === 0) {
        throw new MissingOperandException();
    }
    if (stackOperands.length > 1) {
        throw new MissingOperationException();
    }
    return stackOperands[0];
}

function parsePostfix(s) {
    let stackOperands = [], stackNumberArgs = [];
    let balance = 0;

    for (let i = 0; i < s.length;) {
        while (i < s.length && s[i] === " ") {
            i++;
        }
        if (i === s.length) {
            break;
        }
        let curToken = "", startPos = i, curOperation;
        if (s[i] === '(') {
            balance++;
            i++;
            stackNumberArgs.push(0);
            continue;
        }
        while (i < s.length && s[i] !== ' ' && s[i] !== '(' && s[i] !== ')') {
            curToken += s[i];
            i++;
        }
        if (s[i] === s.length) {
            throw new IBSException();
        }

        if (isNumber(curToken)) {
            stackOperands.push(new Const(parseInt(curToken)));
            stackNumberArgs[stackNumberArgs.length - 1]++;
            continue;
        } else {
            if (isVariable(curToken)) {
                stackOperands.push(new Variable(curToken));
                stackNumberArgs[stackNumberArgs.length - 1]++;
                continue;
            }
        }

        while (i < s.length && s[i] === " ") {
            i++;
        }

        if (i === s.length) {
            throw new IBSException();
        }

        if (s[i] === ')') {
            balance--;
            if (balance < 0) {
                throw new IBSException();
            }
            curOperation = getOperation(curToken);
            if (curOperation === false) {
                throw new UnknownOperationException(startPos);
            }
            let needArg = getNumberOfArguments(curOperation);
            if (needArg === false) {
                needArg = stackNumberArgs[stackNumberArgs.length - 1];
            }
            if (stackOperands.length < needArg) {
                throw new MissingOperandException(startPos);
            }
            let curOpsArgs = [];
            for (let i = stackOperands.length - needArg; i < stackOperands.length; i++) {
                curOpsArgs.push(stackOperands[i]);
            }
            while (needArg > 0) {
                stackOperands.pop();
                needArg--;
            }
            stackOperands.push(new curOperation(...curOpsArgs));
            stackNumberArgs.pop();
            stackNumberArgs[stackNumberArgs.length - 1]++;
            i++;
        } else {
            throw new IncorrectOperandException(startPos);
        }
    }
    if (balance !== 0) {
        throw new IBSException();
    }
    if (stackOperands.length === 0) {
        throw new MissingOperandException();
    }
    if (stackOperands.length > 1) {
        throw new MissingOperationException();
    }
    return stackOperands[0];
}



