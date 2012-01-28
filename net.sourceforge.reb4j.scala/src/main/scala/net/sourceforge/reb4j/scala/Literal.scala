package net.sourceforge.reb4j.scala

@SerialVersionUID(1L)
case class Literal (val unescaped : String) extends Expression 
	with Alternative
{
	require (unescaped != null, "unescaped is null")
	lazy val escaped = Literal.escape(unescaped)
	override def toString = escaped
	override def + (right : Alternative) = right match
	{
		case Literal(rhs) => new Literal(unescaped + rhs)
		case Raw(rhs) => new Raw(escaped + rhs)
		case _ => super.+(right)
	}
	def + (right : Literal) = new Literal(unescaped + right.unescaped)
	def then (right : Literal) = this + right
}


object Literal
{
	def char(unescaped : Char) = new Literal(unescaped.toString()) with Quantifiable
	val needsEscape = "()[]{}.,-\\|+*?$^&:!<>="
	def escapeChar(c : Char) = if (needsEscape.contains(c)) "\\" + c else String.valueOf(c)
	def escape(unescaped : Seq[Char]) = 
		(unescaped.map(escapeChar) addString (new StringBuilder)).toString
}